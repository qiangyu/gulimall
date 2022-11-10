package cn.zqyu.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.zqyu.gulimall.ware.vo.MergePurchaseItemVo;
import cn.zqyu.gulimall.ware.vo.PurchaseDoneVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import cn.zqyu.gulimall.ware.entity.PurchaseEntity;
import cn.zqyu.gulimall.ware.service.PurchaseService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.R;



/**
 * 采购单
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:59:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("ware/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/done")
    public R purchaseDone(@RequestBody PurchaseDoneVo purchaseDoneVo) {
        purchaseService.purchaseDone(purchaseDoneVo);
        return R.ok();
    }

    @PostMapping("/received")
    public R received(@RequestBody List<Long> purchaseIds) {
        purchaseService.received(purchaseIds);
        return R.ok();
    }

    @PostMapping("/merge")
    public R mergePurchaseItem(@RequestBody MergePurchaseItemVo mergePurchaseItemVo) {
        purchaseService.mergePurchaseItem(mergePurchaseItemVo);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * <p>
     * 获取新建和已分配状态的采购单
     *
     * </p>
     *
     * @param params params
     * @return cn.zqyu.common.utils.R /
     * @author zq yu 
     * @since 2022/11/10 15:53 
     */
    @RequestMapping("/unreceive/list")
    public R getUnReceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnReceiveList(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
