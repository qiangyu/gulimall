package cn.zqyu.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.product.dao.AttrGroupDao;
import cn.zqyu.gulimall.product.entity.AttrGroupEntity;
import cn.zqyu.gulimall.product.service.AttrGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params, String catelogId) {

        LambdaQueryWrapper<AttrGroupEntity> queryWrapper = Wrappers.lambdaQuery(AttrGroupEntity.class)
                .orderByAsc(AttrGroupEntity::getSort);

        if (StrUtil.isNotBlank(catelogId)) {
            queryWrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
        }

        String attrGroupName = (String) params.get("key");
        if (StrUtil.isNotBlank(attrGroupName)) {
            queryWrapper.likeRight(AttrGroupEntity::getAttrGroupName, attrGroupName);
        }

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}