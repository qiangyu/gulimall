package cn.zqyu.gulimall.ware.dao;

import cn.zqyu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * εεεΊε­
 * 
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:59:49
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    int addStockBatchById(@Param("wareSkuList") List<WareSkuEntity> wareSkuList);
	
}
