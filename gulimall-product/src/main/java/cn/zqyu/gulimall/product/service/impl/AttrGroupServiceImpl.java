package cn.zqyu.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.product.dao.AttrGroupDao;
import cn.zqyu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import cn.zqyu.gulimall.product.entity.AttrGroupEntity;
import cn.zqyu.gulimall.product.service.AttrAttrgroupRelationService;
import cn.zqyu.gulimall.product.service.AttrGroupService;
import cn.zqyu.gulimall.product.vo.AttrRelationVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service("attrGroupService")
@RequiredArgsConstructor
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

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

        if (StrUtil.isNotBlank(catelogId) && !"0".equals(catelogId)) {
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

    @Override
    public boolean saveBatchAttrRelation(AttrRelationVo[] attrRelationVos) {

        List<AttrAttrgroupRelationEntity> collect = Arrays.stream(attrRelationVos).map(attrRelationVo -> {

            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(attrRelationVo, relation);
            relation.setAttrSort(0);
            return relation;
        }).collect(Collectors.toList());

        return attrAttrgroupRelationService.saveBatch(collect);
    }

    @Override
    public boolean deleteBatchAttrRelation(AttrRelationVo[] attrRelationVos) {
        // 为什么要分组？因为安装摸钱的需求来说，需要删除的分组都是同一个分组
        Map<Long, List<Long>> groupIdAttrIdsMap = Arrays.stream(attrRelationVos).collect(Collectors.groupingBy(AttrRelationVo::getAttrGroupId, Collectors.mapping(AttrRelationVo::getAttrId, Collectors.toList())));
        // 根据分组批量删除该分组的关联的属性
        groupIdAttrIdsMap.forEach((groupId, attIds) -> attrAttrgroupRelationService.remove(
                Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                        .eq(AttrAttrgroupRelationEntity::getAttrGroupId, groupId)
                        .in(AttrAttrgroupRelationEntity::getAttrId, attIds)
        ));

        return true;
    }
}