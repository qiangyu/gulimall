package cn.zqyu.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * δΈι’εε
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:57:49
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

