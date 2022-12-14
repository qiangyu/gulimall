package cn.zqyu.gulimall.ware.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.constant.ware.PurchaseConstant;
import cn.zqyu.common.entity.product.SkuInfoEntity;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.common.utils.R;
import cn.zqyu.gulimall.ware.dao.WareSkuDao;
import cn.zqyu.gulimall.ware.entity.WareSkuEntity;
import cn.zqyu.gulimall.ware.feign.SkuInfoFeign;
import cn.zqyu.gulimall.ware.service.WareSkuService;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    private final WareSkuDao wareSkuDao;

    private final SkuInfoFeign skuInfoFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery(WareSkuEntity.class);

        String skuId = (String) params.get("skuId");
        if (StrUtil.isNotBlank(skuId) && !StrUtil.equals("0", skuId)) {
            queryWrapper.eq(WareSkuEntity::getSkuId, skuId);
        }
        String wareId = (String) params.get("wareId");
        if (StrUtil.isNotBlank(wareId) && !StrUtil.equals("0", wareId)) {
            queryWrapper.eq(WareSkuEntity::getWareId, wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addStockBatch(List<WareSkuEntity> wareSkuList) {
        if (wareSkuList == null || wareSkuList.isEmpty()) {
            return false;
        }

        boolean allMatchIdNotNull = wareSkuList.stream().allMatch(item -> item.getSkuId() != null);
        if (!allMatchIdNotNull) {
            return false;
        }

        boolean allMatchStockNotNull = wareSkuList.stream().allMatch(item -> item.getStock() != null);
        if (!allMatchStockNotNull) {
            return false;
        }

        // 从数据库中根据sku id查询出库存信息
        List<Long> skuIds = wareSkuList.stream().map(WareSkuEntity::getSkuId).collect(Collectors.toList());
        List<WareSkuEntity> dbWareSkuList = this.list(
                Wrappers.lambdaQuery(WareSkuEntity.class)
                        .in(WareSkuEntity::getSkuId, skuIds)
        );
        // sku id：id封装map
        Map<Long, Long> skuIdWareIdMap = Optional.ofNullable(dbWareSkuList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toMap(WareSkuEntity::getSkuId, WareSkuEntity::getId));

        // 远程调用product服务，根据sku id获取sku详细信息
        List<SkuInfoEntity> skuInfoList = null;
        try {
            R feignSkuInfo = skuInfoFeign.getSkuInfo(skuIds);
            if (feignSkuInfo.getCode() == 0) {
                skuInfoList = feignSkuInfo.getData("skuInfoList", new TypeReference<List<SkuInfoEntity>>(){});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // id：name封装map
        Map<Long, String> skuIdNameMap = Optional.ofNullable(skuInfoList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toMap(SkuInfoEntity::getSkuId, SkuInfoEntity::getSkuName));

        // 当map不包含sku id时，是新增sku库存
        List<WareSkuEntity> notSkuCollect = wareSkuList.stream()
                .filter(item -> !skuIdWareIdMap.containsKey(item.getSkuId()))
                .peek(item -> {
                    // 设置sku的名称
                    item.setSkuName(skuIdNameMap.get(item.getSkuId()));
                })
                .collect(Collectors.toList());
        this.saveBatch(notSkuCollect);

        // 当map包含sku id时，是追加库存操作
        List<WareSkuEntity> skuCollect = wareSkuList.stream()
                .filter(item -> skuIdWareIdMap.containsKey(item.getSkuId()))
                .peek(item -> item.setId(skuIdWareIdMap.get(item.getSkuId())))
                .collect(Collectors.toList());
        if (!skuCollect.isEmpty()) {
            wareSkuDao.addStockBatchById(skuCollect);
        }

        return true;
    }

}