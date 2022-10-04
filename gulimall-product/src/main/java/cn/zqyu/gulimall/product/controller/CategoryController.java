package cn.zqyu.gulimall.product.controller;

import cn.zqyu.common.utils.R;
import cn.zqyu.common.vo.product.CategoryEntityVO;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import cn.zqyu.gulimall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品三级分类
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("product/category")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/list/tree")
    public R list(@RequestParam Map<String, Object> params) {
        List<CategoryEntityVO> categoryTree = categoryService.getCategoryTree();

        return R.ok().put("data", categoryTree);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 批量修改
     */
    @PostMapping("/update/sort ")
    public R update(@RequestBody CategoryEntity[] category) {
        categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeByIds(Arrays.asList(catIds));
        List<CategoryEntity> notDelCategoryList = categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok().put("data", notDelCategoryList);
    }

}
