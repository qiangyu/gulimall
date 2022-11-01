package cn.zqyu.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import cn.zqyu.gulimall.product.vo.AttrVo;
import cn.zqyu.gulimall.product.vo.AttrRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.zqyu.gulimall.product.service.AttrService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.R;



/**
 * 商品属性
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("product/attr")
public class AttrController {

    private final AttrService attrService;

    @RequestMapping("/{attrType}/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId,
                  @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, attrType);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attrRespVo = attrService.getAttrDetail(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo){
		attrService.saveDetail(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateDetail(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
