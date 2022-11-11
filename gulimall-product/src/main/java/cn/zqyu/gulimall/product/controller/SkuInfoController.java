package cn.zqyu.gulimall.product.controller;

import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.R;
import cn.zqyu.gulimall.product.entity.SkuInfoEntity;
import cn.zqyu.gulimall.product.service.SkuInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * sku信息
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("product/skuinfo")
public class SkuInfoController {

    private final SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

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
    @PostMapping("/info")
    public R getSkuInfo(List<Long> skuIds){
        List<SkuInfoEntity> list = skuInfoService.listByIds(skuIds);

        return R.ok().put("skuInfoList", list);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
