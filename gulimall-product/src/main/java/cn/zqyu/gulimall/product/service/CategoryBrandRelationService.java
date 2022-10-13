package cn.zqyu.gulimall.product.service;

import cn.zqyu.gulimall.product.entity.BrandEntity;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.product.entity.CategoryBrandRelationEntity;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

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
    boolean saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * <p>
     * 新增品牌信息到品牌表
     * 更新关系表品牌的冗余数据
     *
     * </p>
     *
     * @param brandEntity brandEntity
     * @return boolean /
     * @author zq yu
     * @since 2022/10/13 12:00
     */
    boolean updateBrandInfo(@NotNull BrandEntity brandEntity);

    /**
     * <p>
     * 新增分类信息到分类表
     * 更新关系表分类的冗余数据
     *
     * </p>
     *
     * @param categoryEntity categoryEntity
     * @return boolean /
     * @author zq yu
     * @since 2022/10/13 13:49
     */
    boolean updateCategoryInfo(CategoryEntity categoryEntity);
}

