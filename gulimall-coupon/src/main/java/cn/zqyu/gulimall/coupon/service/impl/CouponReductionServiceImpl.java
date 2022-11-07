package cn.zqyu.gulimall.coupon.service.impl;

import cn.zqyu.common.to.product.SkuReductionTo;
import cn.zqyu.common.to.product.SpuBoundTo;
import cn.zqyu.gulimall.coupon.entity.MemberPriceEntity;
import cn.zqyu.gulimall.coupon.entity.SkuFullReductionEntity;
import cn.zqyu.gulimall.coupon.entity.SkuLadderEntity;
import cn.zqyu.gulimall.coupon.entity.SpuBoundsEntity;
import cn.zqyu.gulimall.coupon.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.coupon.service.impl
 * @see CouponReductionServiceImpl
 * @since 2022/11/1 8:50
 */
@RequiredArgsConstructor
@Service
public class CouponReductionServiceImpl implements CouponReductionService {

    private final SpuBoundsService spuBoundsService;
    private final SkuFullReductionService skuFullReductionService;
    private final SkuLadderService skuLadderService;
    private final MemberPriceService memberPriceService;

    @Override
    public void saveBoundsInfo(SpuBoundTo spuBoundTo) {
        SpuBoundsEntity spuBoundsEntity = new SpuBoundsEntity();
        BeanUtils.copyProperties(spuBoundTo, spuBoundsEntity);
        spuBoundsService.save(spuBoundsEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveReductionInfo(SkuReductionTo skuReductionTo) {
        // 保存满几件，打几折信息
        if (skuReductionTo.getFullCount() > 0) {
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
            skuFullReductionEntity.setAddOther(skuReductionTo.getCountStatus());
            skuFullReductionService.save(skuFullReductionEntity);
        }

        // 保存满金额，优惠多少金额
        if (skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) >= 1) {
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            BeanUtils.copyProperties(skuReductionTo, skuLadderEntity);
            skuLadderEntity.setAddOther(skuReductionTo.getPriceStatus());
            skuLadderService.save(skuLadderEntity);
        }

        // 保存会员价格
        List<MemberPriceEntity> memberPriceEntityList = Optional.ofNullable(skuReductionTo.getMemberPrice()).map(List::stream).orElseGet(Stream::empty)
                .map(memberPrice -> {
                    MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                    memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                    memberPriceEntity.setMemberLevelId(memberPrice.getId());
                    memberPriceEntity.setMemberLevelName(memberPrice.getName());
                    memberPriceEntity.setMemberPrice(memberPrice.getPrice());
                    return memberPriceEntity;
                })
                .filter(memberPrice -> memberPrice.getMemberPrice().compareTo(new BigDecimal(0)) >= 1)
                .collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);

    }

}
