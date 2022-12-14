package cn.zqyu.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.to.product.SkuReductionTo;
import cn.zqyu.common.to.product.SpuBoundTo;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.common.utils.R;
import cn.zqyu.common.vo.product.*;
import cn.zqyu.gulimall.product.dao.SpuInfoDao;
import cn.zqyu.gulimall.product.entity.*;
import cn.zqyu.gulimall.product.feign.FeignCoupon;
import cn.zqyu.gulimall.product.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    private final SpuInfoDescService spuInfoDescService;
    private final SpuImagesService spuImagesService;

    private final AttrService attrService;

    private final ProductAttrValueService productAttrValueService;

    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;

    private final FeignCoupon feignCoupon;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SpuInfoEntity> queryWrapper = Wrappers.lambdaQuery(SpuInfoEntity.class);

        String key = (String) params.get("key");
        if (StrUtil.isNotBlank(key)) {
            queryWrapper.likeRight(SpuInfoEntity::getSpuName, key);
        }
        String status = (String) params.get("status");
        if (StrUtil.isNotBlank(status)) {
            queryWrapper.eq(SpuInfoEntity::getPublishStatus, status);
        }
        String brandId = (String) params.get("brandId");
        if (StrUtil.isNotBlank(brandId) && !StrUtil.equals("0", brandId)) {
            queryWrapper.eq(SpuInfoEntity::getBrandId, brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (StrUtil.isNotBlank(catelogId) && !StrUtil.equals("0", catelogId)) {
            queryWrapper.eq(SpuInfoEntity::getCatalogId, catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDetailInfo(SpuSaveVO spuInfoVo) {
        // ??????spu???????????????
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfoVo, spuInfoEntity);
        this.save(spuInfoEntity);

        // ??????spu???????????????
        List<String> decriptList = spuInfoVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decriptList));
        spuInfoDescService.save(spuInfoDescEntity);

        // ??????spu????????????
        List<String> imageList = spuInfoVo.getImages();
        List<SpuImagesEntity> imagesEntityList = Optional.ofNullable(imageList).map(List::stream).orElseGet(Stream::empty)
                .map(image -> {
                    SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                    spuImagesEntity.setSpuId(spuInfoEntity.getId());
                    spuImagesEntity.setImgUrl(image);
                    return spuImagesEntity;
                }).collect(Collectors.toList());
        spuImagesService.saveBatch(imagesEntityList);

        // ??????spu?????????????????????
        List<BaseAttrs> baseAttrsList = spuInfoVo.getBaseAttrs();
        if (baseAttrsList != null && !baseAttrsList.isEmpty()) {
            List<Long> attrIds = baseAttrsList.stream().map(BaseAttrs::getAttrId).collect(Collectors.toList());
            List<AttrEntity> attrEntityList = attrService.listByIds(attrIds);
            if (attrEntityList != null && !attrEntityList.isEmpty()) {

                Map<Long, String> attrIdNameMap = attrEntityList.stream().collect(Collectors.toMap(AttrEntity::getAttrId, AttrEntity::getAttrName));

                List<ProductAttrValueEntity> collect = baseAttrsList.stream()
                        .map(baseAttr -> {
                            ProductAttrValueEntity entity = new ProductAttrValueEntity();
                            entity.setSpuId(spuInfoEntity.getId());
                            entity.setAttrId(baseAttr.getAttrId());
                            entity.setAttrName(attrIdNameMap.get(baseAttr.getAttrId()));
                            entity.setAttrValue(baseAttr.getAttrValues());
                            entity.setQuickShow(baseAttr.getShowDesc());
                            return entity;
                        }).collect(Collectors.toList());
                productAttrValueService.saveBatch(collect);
            }
        }

        // TODO ????????????
        Bounds infoVoBounds = spuInfoVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(infoVoBounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R responseBounds = feignCoupon.saveBounds(spuBoundTo);
        if (responseBounds.getCode() != 0) {
            throw new IllegalArgumentException("??????feign??????bounds??????");
        }

        List<Skus> skusList = spuInfoVo.getSkus();
        for (Skus skus : skusList) {

            // ???????????????????????????
            Optional<Images> firstImages = Optional.ofNullable(skus.getImages()).map(List::stream).orElseGet(Stream::empty)
                    .filter(images -> StrUtil.isNotBlank(images.getImgUrl()) && images.getDefaultImg() == 1)
                    .findFirst();
            String defaultImg = firstImages.isPresent() ? firstImages.get().getImgUrl() : "";

            // ??????sku???????????????
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(skus, skuInfoEntity);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSkuDefaultImg(defaultImg);
            skuInfoService.save(skuInfoEntity);

            // ??????sku????????????
            List<SkuImagesEntity> skuImagesEntityList = Optional.ofNullable(skus.getImages()).map(List::stream).orElseGet(Stream::empty)
                    .filter(images -> StrUtil.isNotBlank(images.getImgUrl()))
                    .map(images -> {
                        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                        skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                        skuImagesEntity.setImgUrl(images.getImgUrl());
                        skuImagesEntity.setDefaultImg(images.getDefaultImg());
                        return skuImagesEntity;
                    }).collect(Collectors.toList());
            skuImagesService.saveBatch(skuImagesEntityList);

            // ??????????????????
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = Optional.ofNullable(skus.getAttr()).map(List::stream).orElseGet(Stream::empty)
                    .map(attr -> {
                        SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                        BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                        skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                        return skuSaleAttrValueEntity;
                    }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

            // TODO ?????????????????????????????????
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(skus, skuReductionTo);
            R responseCoupon = feignCoupon.saveCoupon(skuReductionTo);
            if (responseCoupon.getCode() != 0) {
                throw new IllegalArgumentException("??????feign??????coupon??????");
            }
        }

    }

}