package cn.zqyu.gulimall.coupon.dao;

import cn.zqyu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:57:50
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
