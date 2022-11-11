package cn.zqyu.gulimall.ware.service.impl;

import cn.zqyu.common.constant.ware.PurchaseConstant;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.ware.dao.PurchaseDao;
import cn.zqyu.gulimall.ware.entity.PurchaseDetailEntity;
import cn.zqyu.gulimall.ware.entity.PurchaseEntity;
import cn.zqyu.gulimall.ware.entity.WareSkuEntity;
import cn.zqyu.gulimall.ware.service.PurchaseDetailService;
import cn.zqyu.gulimall.ware.service.PurchaseService;
import cn.zqyu.gulimall.ware.service.WareSkuService;
import cn.zqyu.gulimall.ware.vo.MergePurchaseItemVo;
import cn.zqyu.gulimall.ware.vo.PurchaseDoneVo;
import cn.zqyu.gulimall.ware.vo.PurchaseItemDoneVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    private final PurchaseDetailService purchaseDetailService;

    private final WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnReceiveList(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                Wrappers.lambdaQuery(PurchaseEntity.class)
                        .eq(PurchaseEntity::getStatus, PurchaseConstant.PurchaseStatusEnum.CREATED.getCode())
                        .or()
                        .eq(PurchaseEntity::getStatus, PurchaseConstant.PurchaseStatusEnum.ASSIGNED.getCode())
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean mergePurchaseItem(MergePurchaseItemVo mergePurchaseItemVo) {
        Long purchaseId = mergePurchaseItemVo.getPurchaseId();

        List<Long> itemIds = mergePurchaseItemVo.getItems();
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("采购项为空");
        }

        PurchaseEntity dbPurchase = this.getById(purchaseId);

        if (dbPurchase == null) {
            // TODO 仓库id这里没保存，以及页面创建的采购单也没有保存其仓库id
            // 要么在页面添加仓库的选项，或者仓库表加一个字段，标识是否为默认仓库
            // 创建新的采购单
            dbPurchase = PurchaseEntity.builder()
                    .status(PurchaseConstant.PurchaseStatusEnum.CREATED.getCode())
                    .priority(1)
                    .build();
            this.save(dbPurchase);
            purchaseId = dbPurchase.getId();
        }
        Integer status = dbPurchase.getStatus();
        if (!PurchaseConstant.PurchaseStatusEnum.CREATED.getCode().equals(status)
                &&
                !PurchaseConstant.PurchaseStatusEnum.ASSIGNED.getCode().equals(status)) {
            // 采购单不能再次合并
            throw new IllegalArgumentException("采购单的状态不能再合并采购项");
        }

        // 需要判断采购项是否被分配到采购单
        List<PurchaseDetailEntity> dbPurchaseDetailList = purchaseDetailService.listByIds(itemIds);
        boolean allMatch = Optional.ofNullable(dbPurchaseDetailList).map(List::stream).orElseGet(Stream::empty)
                .allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.CREATED.getCode().equals(item.getStatus()));
        if (!allMatch) {
            throw new IllegalArgumentException("非法采购项，采购项的状态不是: ".concat(PurchaseConstant.PurchaseDetailStatusEnum.CREATED.getMsg()));
        }

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = itemIds.stream()
                .map(itemId -> PurchaseDetailEntity.builder()
                        .id(itemId)
                        .purchaseId(finalPurchaseId)
                        .status(PurchaseConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())
                        .build())
                .collect(Collectors.toList());

        return purchaseDetailService.updateBatchById(purchaseDetailEntityList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean received(List<Long> purchaseIds) {
        List<PurchaseEntity> purchaseList = this.listByIds(purchaseIds);

        if (purchaseList == null || purchaseList.isEmpty()) {
            throw new IllegalArgumentException("非法采购单");
        }
        if (purchaseList.size() != purchaseIds.size()) {
            throw new IllegalArgumentException("采购单列表中有非法采购单");
        }
        boolean allMatch = purchaseList.stream().allMatch(item -> PurchaseConstant.PurchaseStatusEnum.ASSIGNED.getCode().equals(item.getStatus()));
        if (!allMatch) {
            throw new IllegalArgumentException("采购单列表中有状态不是: ".concat(PurchaseConstant.PurchaseStatusEnum.ASSIGNED.getMsg()));
        }
        // 更改采购单的状态
        List<PurchaseEntity> updatePurchaseCollect = purchaseIds.stream().map(purchaseId -> PurchaseEntity.builder()
                .id(purchaseId)
                .status(PurchaseConstant.PurchaseStatusEnum.RECEIVE.getCode())
                .build()).collect(Collectors.toList());
        this.updateBatchById(updatePurchaseCollect);

        List<PurchaseDetailEntity> purchaseDetailList = purchaseDetailService.list(
                Wrappers.lambdaQuery(PurchaseDetailEntity.class)
                        .in(PurchaseDetailEntity::getPurchaseId, purchaseIds)
        );
        if (purchaseDetailList == null || purchaseDetailList.isEmpty()) {
            throw new IllegalArgumentException("采购项不存在");
        }
        boolean purchaseDetailAllMatch = purchaseDetailList.stream().allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode().equals(item.getStatus()));
        if (!purchaseDetailAllMatch) {
            throw new IllegalArgumentException("采购项状态不正常，请联系管理员");
        }

        List<PurchaseDetailEntity> updatePurchaseDetailCollect = purchaseDetailList.stream().map(item -> PurchaseDetailEntity.builder()
                .id(item.getId())
                .status(PurchaseConstant.PurchaseDetailStatusEnum.BUYING.getCode())
                .build()).collect(Collectors.toList());
        return purchaseDetailService.updateBatchById(updatePurchaseDetailCollect);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean purchaseDone(PurchaseDoneVo purchaseDoneVo) {
        Long purchaseId = purchaseDoneVo.getId();
        if (purchaseId == null) {
            throw new IllegalArgumentException("采购单id为null");
        }
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("采购项为空，如果全部失败请选择失败理由");
        }

        PurchaseEntity purchase = this.getById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("采购单不存在");
        }
        if (!PurchaseConstant.PurchaseStatusEnum.RECEIVE.getCode().equals(purchase.getStatus())) {
            throw new IllegalArgumentException("采购单的状态不是: ".concat(PurchaseConstant.PurchaseStatusEnum.RECEIVE.getMsg()));
        }

        List<PurchaseDetailEntity> purchaseDetailList = purchaseDetailService.list(
                Wrappers.lambdaQuery(PurchaseDetailEntity.class)
                        .eq(PurchaseDetailEntity::getPurchaseId, purchaseId)
        );
        if (purchaseDetailList == null || purchaseDetailList.size() != items.size()) {
            throw new IllegalArgumentException("采购项与完成的采购项数量不符");
        }

        // 过滤是否全部采购项正常采购
        boolean purchaseStatusAllMatch = purchaseDetailList.stream().allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.BUYING.getCode().equals(item.getStatus()));
        if (!purchaseStatusAllMatch) {
            throw new IllegalArgumentException("采购项中有状态不是: ".concat(PurchaseConstant.PurchaseDetailStatusEnum.BUYING.getMsg()).concat("，请联系管理员"));
        }

        boolean isAllFinish = items.stream().allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.FINISH.getCode().equals(item.getStatus()));

        // 更新采购单状态
        this.updateById(PurchaseEntity.builder()
                .id(purchaseId)
                .status(isAllFinish ? PurchaseConstant.PurchaseStatusEnum.FINISH.getCode() : PurchaseConstant.PurchaseStatusEnum.HASERROR.getCode())
                .build());

        // 更新采购项状态
        List<PurchaseDetailEntity> updatePurchaseDetailCollect = items.stream()
                .map(item -> PurchaseDetailEntity.builder()
                        .id(item.getItemId())
                        .status(item.getStatus())
                        .build())
                .collect(Collectors.toList());
        purchaseDetailService.updateBatchById(updatePurchaseDetailCollect);

        // TODO 如果采购失败，其原因没保存，如果实际采购数和所需采购数不用，也需要做调整
        // 这里先把所有的采购项传过去保存
        // 封装库存信息
        List<WareSkuEntity> wareSkuList = purchaseDetailList.stream()
                .map(item -> WareSkuEntity.builder()
                        .skuId(item.getSkuId())
                        .wareId(item.getWareId())
                        .stock(item.getSkuNum())
                        .build())
                .collect(Collectors.toList());
        // 添加采购项的库存
        wareSkuService.addStockBatch(wareSkuList);


        return true;
    }

}