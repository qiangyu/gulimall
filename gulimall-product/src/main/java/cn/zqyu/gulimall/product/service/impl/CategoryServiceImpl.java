package cn.zqyu.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.common.vo.product.CategoryEntityVO;
import cn.zqyu.gulimall.product.dao.CategoryDao;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import cn.zqyu.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntityVO> getCategoryTree() {

        List<CategoryEntity> allCategoryList = list();
        if (allCategoryList == null || allCategoryList.isEmpty()) {
            return null;
        }
        // 以及菜单
        return allCategoryList.stream()
                .filter(categoryEntity -> categoryEntity.getCatLevel().equals(1))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .map(categoryEntity -> {
                    CategoryEntityVO vo = new CategoryEntityVO();

                    BeanUtils.copyProperties(categoryEntity, vo);
                    vo.setChildren(getCategoryChildren(vo, allCategoryList));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryEntity> removeMenuByIds(List<Long> ids) {

        List<CategoryEntity> noeDelList = new ArrayList<>();

        if (ids == null || ids.size() == 0) {
            return noeDelList;
        }

        List<CategoryEntity> categoryList = baseMapper.selectList(
                Wrappers.lambdaQuery(CategoryEntity.class)
                        .in(CategoryEntity::getParentCid, ids)
        );

        // 如果有 id 被引用
        if (categoryList != null && !categoryList.isEmpty()) {
            Map<Long, CategoryEntity> categoryMap = categoryList.stream()
                    .collect(Collectors.toMap(CategoryEntity::getCatId, Function.identity()));

            ids.forEach(id -> {
                // 被引用，不能删除
                CategoryEntity category = categoryMap.get(id);
                if (StrUtil.isNotBlank(category.getName())) {
                    noeDelList.add(category);
                    ids.remove(id);
                }
            });
        }

        // 逻辑删除
        baseMapper.deleteBatchIds(ids);

        return noeDelList;
    }

    @Override
    public List<Long> findCatelogPath(Long catelogId) {

        if (catelogId == null) {
            return new ArrayList<>();
        }

        LinkedList<Long> paths = new LinkedList<>();
        CategoryEntity category = baseMapper.selectById(catelogId);

        if (category == null) {
            return new ArrayList<>();
        }
        paths.addFirst(category.getCatId());

        // 递归获取父 category id
        getCategoryParentCid(category, paths);

        return paths;
    }

    private List<Long> getCategoryParentCid(CategoryEntity category, LinkedList<Long> paths) {

        if (category.getParentCid() != null && category.getParentCid() != 0) {
            CategoryEntity parentCategory = baseMapper.selectById(category.getParentCid());
            paths.addFirst(parentCategory.getCatId());
            getCategoryParentCid(parentCategory, paths);
        }

        return paths;
    }

    /**
     * <p>
     * 获取 entity 子孙菜单
     *
     * </p>
     *
     * @param entity          entity
     * @param allCategoryList allCategoryList
     * @return java.util.List<cn.zqyu.common.vo.product.CategoryEntityVO> /
     * @author zq yu
     * @since 2022/9/23 11:41
     */
    private List<CategoryEntityVO> getCategoryChildren(CategoryEntityVO entity, List<CategoryEntity> allCategoryList) {
        return allCategoryList.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(entity.getCatId()))
                .sorted(Comparator.comparing(CategoryEntity::getSort))
                .map(categoryEntity -> {
                    CategoryEntityVO vo = new CategoryEntityVO();
                    BeanUtils.copyProperties(categoryEntity, vo);
                    vo.setChildren(getCategoryChildren(vo, allCategoryList));
                    return vo;
                })
                .collect(Collectors.toList());
    }
}