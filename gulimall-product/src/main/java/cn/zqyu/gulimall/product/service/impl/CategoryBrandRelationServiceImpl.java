package cn.zqyu.gulimall.product.service.impl;

import cn.zqyu.gulimall.product.entity.BrandEntity;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import cn.zqyu.gulimall.product.service.BrandService;
import cn.zqyu.gulimall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;

import cn.zqyu.gulimall.product.dao.CategoryBrandRelationDao;
import cn.zqyu.gulimall.product.entity.CategoryBrandRelationEntity;
import cn.zqyu.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
@RequiredArgsConstructor
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    private final BrandService brandService;
    private final CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * <p>
     * 插入品牌与分类关联详细信息
     *
     * </p>
     *
     * @param categoryBrandRelation categoryBrandRelation
     * @return boolean /
     * @author zq yu
     * @since 2022/10/4 23:44
     */
    @Override
    public boolean saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        BrandEntity brandEntity = brandService.getById(categoryBrandRelation.getBrandId());
        if (brandEntity == null) {
            throw new IllegalArgumentException("brand id is illegal");
        }

        CategoryEntity categoryEntity = categoryService.getById(categoryBrandRelation.getCatelogId());
        if (categoryEntity == null) {
            throw new IllegalArgumentException("category id is illegal");
        }

        // 填充参数
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        return save(categoryBrandRelation);
    }
}