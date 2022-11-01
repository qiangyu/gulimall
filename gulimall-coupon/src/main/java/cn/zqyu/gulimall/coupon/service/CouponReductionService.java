package cn.zqyu.gulimall.coupon.service;

import cn.zqyu.common.to.product.SkuReductionTo;
import cn.zqyu.common.to.product.SpuBoundTo;
import cn.zqyu.common.utils.R;

/**
 * <p>
 * 满减
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.coupon.service
 * @see CouponReductionService
 * @since 2022/11/1 8:50
 */
public interface CouponReductionService {

    /**
     * <p>
     * 保存商品积分信息
     *
     * </p>
     *
     * @param spuBoundTo spuBoundTo
     * @author zq yu
     * @since 2022/11/1 9:40
     */
    void saveBoundsInfo(SpuBoundTo spuBoundTo);

    /**
     * <p>
     * 保存商品满减信息
     *
     * </p>
     *
     * @param skuReductionTo skuReductionTo   /
     * @author zq yu
     * @since 2022/11/1 8:51
     */
    void saveReductionInfo(SkuReductionTo skuReductionTo);

}
