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
            throw new IllegalArgumentException("???????????????");
        }

        PurchaseEntity dbPurchase = this.getById(purchaseId);

        if (dbPurchase == null) {
            // TODO ??????id????????????????????????????????????????????????????????????????????????id
            // ???????????????????????????????????????????????????????????????????????????????????????????????????
            // ?????????????????????
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
            // ???????????????????????????
            throw new IllegalArgumentException("??????????????????????????????????????????");
        }

        // ????????????????????????????????????????????????
        List<PurchaseDetailEntity> dbPurchaseDetailList = purchaseDetailService.listByIds(itemIds);
        boolean allMatch = Optional.ofNullable(dbPurchaseDetailList).map(List::stream).orElseGet(Stream::empty)
                .allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.CREATED.getCode().equals(item.getStatus()));
        if (!allMatch) {
            throw new IllegalArgumentException("??????????????????????????????????????????: ".concat(PurchaseConstant.PurchaseDetailStatusEnum.CREATED.getMsg()));
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
            throw new IllegalArgumentException("???????????????");
        }
        if (purchaseList.size() != purchaseIds.size()) {
            throw new IllegalArgumentException("????????????????????????????????????");
        }
        boolean allMatch = purchaseList.stream().allMatch(item -> PurchaseConstant.PurchaseStatusEnum.ASSIGNED.getCode().equals(item.getStatus()));
        if (!allMatch) {
            throw new IllegalArgumentException("?????????????????????????????????: ".concat(PurchaseConstant.PurchaseStatusEnum.ASSIGNED.getMsg()));
        }
        // ????????????????????????
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
            throw new IllegalArgumentException("??????????????????");
        }
        boolean purchaseDetailAllMatch = purchaseDetailList.stream().allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode().equals(item.getStatus()));
        if (!purchaseDetailAllMatch) {
            throw new IllegalArgumentException("?????????????????????????????????????????????");
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
            throw new IllegalArgumentException("?????????id???null");
        }
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("?????????????????????????????????????????????????????????");
        }

        PurchaseEntity purchase = this.getById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("??????????????????");
        }
        if (!PurchaseConstant.PurchaseStatusEnum.RECEIVE.getCode().equals(purchase.getStatus())) {
            throw new IllegalArgumentException("????????????????????????: ".concat(PurchaseConstant.PurchaseStatusEnum.RECEIVE.getMsg()));
        }

        List<PurchaseDetailEntity> purchaseDetailList = purchaseDetailService.list(
                Wrappers.lambdaQuery(PurchaseDetailEntity.class)
                        .eq(PurchaseDetailEntity::getPurchaseId, purchaseId)
        );
        if (purchaseDetailList == null || purchaseDetailList.size() != items.size()) {
            throw new IllegalArgumentException("??????????????????????????????????????????");
        }

        // ???????????????????????????????????????
        boolean purchaseStatusAllMatch = purchaseDetailList.stream().allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.BUYING.getCode().equals(item.getStatus()));
        if (!purchaseStatusAllMatch) {
            throw new IllegalArgumentException("???????????????????????????: ".concat(PurchaseConstant.PurchaseDetailStatusEnum.BUYING.getMsg()).concat("?????????????????????"));
        }

        boolean isAllFinish = items.stream().allMatch(item -> PurchaseConstant.PurchaseDetailStatusEnum.FINISH.getCode().equals(item.getStatus()));

        // ?????????????????????
        this.updateById(PurchaseEntity.builder()
                .id(purchaseId)
                .status(isAllFinish ? PurchaseConstant.PurchaseStatusEnum.FINISH.getCode() : PurchaseConstant.PurchaseStatusEnum.HASERROR.getCode())
                .build());

        // ?????????????????????
        List<PurchaseDetailEntity> updatePurchaseDetailCollect = items.stream()
                .map(item -> PurchaseDetailEntity.builder()
                        .id(item.getItemId())
                        .status(item.getStatus())
                        .build())
                .collect(Collectors.toList());
        purchaseDetailService.updateBatchById(updatePurchaseDetailCollect);

        // TODO ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ?????????????????????????????????????????????
        // ??????????????????
        List<WareSkuEntity> wareSkuList = purchaseDetailList.stream()
                .map(item -> WareSkuEntity.builder()
                        .skuId(item.getSkuId())
                        .wareId(item.getWareId())
                        .stock(item.getSkuNum())
                        .build())
                .collect(Collectors.toList());
        // ????????????????????????
        wareSkuService.addStockBatch(wareSkuList);


        return true;
    }

}