package cn.zqyu.gulimall.ware.feign;

import cn.zqyu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * sku的远程调用
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.ware.feign
 * @see SkuInfoFeign
 * @since 2022/11/11 11:07
 */
@FeignClient("gulimall-product")
public interface SkuInfoFeign {

    /**
     * <p>
     * 根据sku ids集合获取sku详细信息
     *
     * </p>
     *
     * @param skuIds skuIds
     * @return cn.zqyu.common.utils.R /
     * @author zq yu
     * @since 2022/11/11 11:07
     */
    @PostMapping("/product/skuinfo/info/list")
    R getSkuInfo(@RequestBody List<Long> skuIds);

}
