package cn.zqyu.gulimall.coupon.feign;

import cn.zqyu.common.to.product.SkuReductionTo;
import cn.zqyu.common.to.product.SpuBoundTo;
import cn.zqyu.common.utils.R;
import cn.zqyu.gulimall.coupon.service.CouponReductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 优惠的feign接口
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.coupon.feign
 * @see FeignCouponController
 * @since 2022/11/1 8:46
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("coupon/feign")
public class FeignCouponController {

    private final CouponReductionService couponReductionService;

    @PostMapping("/bounds")
    R saveBounds(@RequestBody SpuBoundTo spuBoundTo) {
        couponReductionService.saveBoundsInfo(spuBoundTo);
        return R.ok();
    }

    @PostMapping("/reduction")
    R saveCoupon(@RequestBody SkuReductionTo skuReductionTo) {
        couponReductionService.saveReductionInfo(skuReductionTo);
        return R.ok();
    }

}
