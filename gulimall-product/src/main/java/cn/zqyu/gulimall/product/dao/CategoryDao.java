package cn.zqyu.gulimall.product.dao;

import cn.zqyu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:20
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
