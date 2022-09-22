package cn.zqyu.gulimall.ware.dao;

import cn.zqyu.gulimall.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购单
 * 
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:59:49
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
