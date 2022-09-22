package cn.zqyu.gulimall.member.dao;

import cn.zqyu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 23:28:44
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
