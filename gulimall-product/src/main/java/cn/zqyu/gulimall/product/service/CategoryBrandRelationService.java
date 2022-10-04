package cn.zqyu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.product.entity.CategoryBrandRelationEntity;

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

    boolean saveDetail(CategoryBrandRelationEntity categoryBrandRelation);
}

