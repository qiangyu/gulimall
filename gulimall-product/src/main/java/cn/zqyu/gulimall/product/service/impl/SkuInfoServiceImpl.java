package cn.zqyu.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.product.dao.SkuInfoDao;
import cn.zqyu.gulimall.product.entity.SkuInfoEntity;
import cn.zqyu.gulimall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SkuInfoEntity> queryWrapper = Wrappers.lambdaQuery(SkuInfoEntity.class);

        String key = (String) params.get("key");
        if (StrUtil.isNotBlank(key)) {
            queryWrapper.likeRight(SkuInfoEntity::getSkuName, key);
        }
        String brandId = (String) params.get("brandId");
        if (StrUtil.isNotBlank(brandId) && !StrUtil.equals("0", brandId)) {
            queryWrapper.eq(SkuInfoEntity::getBrandId, brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (StrUtil.isNotBlank(catelogId) && !StrUtil.equals("0", catelogId)) {
            queryWrapper.eq(SkuInfoEntity::getCatalogId, catelogId);
        }

        try {
            BigDecimal minPrice = new BigDecimal((String) params.get("min"));
            if (minPrice.compareTo(new BigDecimal("0")) >= 1) {
                queryWrapper.ge(SkuInfoEntity::getPrice, minPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BigDecimal maxPrice = new BigDecimal((String) params.get("max"));
            if (maxPrice.compareTo(new BigDecimal("0")) >= 1) {
                queryWrapper.le(SkuInfoEntity::getPrice, maxPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}