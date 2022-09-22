package cn.zqyu.gulimall.order.dao;

import cn.zqyu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:50:52
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
