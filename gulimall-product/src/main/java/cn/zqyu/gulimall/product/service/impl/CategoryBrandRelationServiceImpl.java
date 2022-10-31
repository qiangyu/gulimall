package cn.zqyu.gulimall.product.service.impl;

import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.product.dao.CategoryBrandRelationDao;
import cn.zqyu.gulimall.product.entity.BrandEntity;
import cn.zqyu.gulimall.product.entity.CategoryBrandRelationEntity;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import cn.zqyu.gulimall.product.service.BrandService;
import cn.zqyu.gulimall.product.service.CategoryBrandRelationService;
import cn.zqyu.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBrandInfo(BrandEntity brandEntity) {

        // 先更新品牌
        brandService.updateById(brandEntity);

        return this.update(Wrappers.lambdaUpdate(CategoryBrandRelationEntity.class)
                .set(CategoryBrandRelationEntity::getBrandName, brandEntity.getName())
                .eq(CategoryBrandRelationEntity::getBrandId, brandEntity.getBrandId()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCategoryInfo(CategoryEntity categoryEntity) {

        // 先更新分类
        categoryService.updateById(categoryEntity);

        return this.update(Wrappers.lambdaUpdate(CategoryBrandRelationEntity.class)
                .set(CategoryBrandRelationEntity::getCatelogName, categoryEntity.getName())
                .eq(CategoryBrandRelationEntity::getCatelogId, categoryEntity.getCatId()));
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {

        List<CategoryBrandRelationEntity> relationList = this.list(
                Wrappers.lambdaQuery(CategoryBrandRelationEntity.class)
                        .eq(CategoryBrandRelationEntity::getCatelogId, catId)
        );

        List<Long> brandIds = relationList.stream().map(CategoryBrandRelationEntity::getBrandId).collect(Collectors.toList());

        return brandService.listByIds(brandIds);
    }
}