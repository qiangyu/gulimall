package cn.zqyu.gulimall.product.feign;

import cn.zqyu.common.to.product.SkuReductionTo;
import cn.zqyu.common.to.product.SpuBoundTo;
import cn.zqyu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.product.feign
 * @see FeignCoupon
 * @since 2022/11/1 9:05
 */
@FeignClient("gulimall-coupon")
public interface FeignCoupon {

    /**
     * <p>
     * 保存商品积分信息
     *
     * </p>
     *
     * @param spuBoundTo spuBoundTo
     * @return cn.zqyu.common.utils.R /
     * @author zq yu
     * @since 2022/11/1 9:44
     */
    @PostMapping("/coupon/feign/bounds")
    R saveBounds(@RequestBody SpuBoundTo spuBoundTo);

    /**
     * <p>
     * 保存商品满减信息
     *
     * </p>
     *
     * @param skuReductionTo skuReductionTo
     * @return cn.zqyu.common.utils.R /
     * @author zq yu
     * @since 2022/11/1 9:44
     */
    @PostMapping("/coupon/feign/reduction")
    R saveCoupon(@RequestBody SkuReductionTo skuReductionTo);

}
