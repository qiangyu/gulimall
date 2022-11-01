package cn.zqyu.gulimall.product.controller;

import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.R;
import cn.zqyu.common.vo.product.AttrGroupVO;
import cn.zqyu.gulimall.product.entity.AttrEntity;
import cn.zqyu.gulimall.product.entity.AttrGroupEntity;
import cn.zqyu.gulimall.product.service.AttrGroupService;
import cn.zqyu.gulimall.product.service.AttrService;
import cn.zqyu.gulimall.product.service.CategoryService;
import cn.zqyu.gulimall.product.vo.AttrGroupWithAttrsVo;
import cn.zqyu.gulimall.product.vo.AttrRelationVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    private final AttrGroupService attrGroupService;

    private final AttrService attrService;

    private final CategoryService categoryService;

    @GetMapping("/{attrgroupId}/attr/relation")
    public R getAttrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> attrEntityList = attrService.getAttrRelation(attrgroupId);
        return R.ok().put("data", attrEntityList);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getAttrNoRelation(@RequestParam Map<String, Object> params,
                               @PathVariable("attrgroupId") Long attrgroupId) {
        PageUtils pageUtils = attrService.getAttrNoRelation(params, attrgroupId);
        return R.ok().put("page", pageUtils);
    }

    @GetMapping("/{catlogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catlogId") Long catlogId) {

        List<AttrGroupWithAttrsVo> list = attrService.getAttrGroupWithAttrsByCatlogId(catlogId);

        return R.ok().put("data", list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") String catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        AttrGroupVO attrGroupVO = new AttrGroupVO();
        BeanUtils.copyProperties(attrGroup, attrGroupVO);

        List<Long> catelogPath = categoryService.findCatelogPath(attrGroupVO.getCatelogId());
        attrGroupVO.setCatelogPath(catelogPath);

        return R.ok().put("attrGroup", attrGroupVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    @PostMapping("/attr/relation")
    public R saveBatchAttrRelation(@RequestBody AttrRelationVo[] attrRelationVos) {
        attrGroupService.saveBatchAttrRelation(attrRelationVos);

        return R.ok();
    }

    @PostMapping("/attr/relation/delete")
    public R deleteBatchAttrRelation(@RequestBody AttrRelationVo[] attrRelationVos) {
        attrGroupService.deleteBatchAttrRelation(attrRelationVos);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
