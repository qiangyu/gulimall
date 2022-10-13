package cn.zqyu.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.product.dao.AttrDao;
import cn.zqyu.gulimall.product.dto.AttrDto;
import cn.zqyu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import cn.zqyu.gulimall.product.entity.AttrEntity;
import cn.zqyu.gulimall.product.entity.AttrGroupEntity;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import cn.zqyu.gulimall.product.service.AttrAttrgroupRelationService;
import cn.zqyu.gulimall.product.service.AttrGroupService;
import cn.zqyu.gulimall.product.service.AttrService;
import cn.zqyu.gulimall.product.service.CategoryService;
import cn.zqyu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("attrService")
@RequiredArgsConstructor
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    private final CategoryService categoryService;
    private final AttrGroupService attrGroupService;
    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {

        LambdaQueryWrapper<AttrEntity> queryWrapper = Wrappers.lambdaQuery(AttrEntity.class);

        if (catelogId != null && !catelogId.equals(0L)) {
            queryWrapper.eq(AttrEntity::getCatelogId, catelogId);
        }

        String key = (String) params.get("key");
        if (StrUtil.isNotBlank(key)) {
            queryWrapper.likeRight(AttrEntity::getAttrName, key);
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        // 获取记录
        List<AttrEntity> records = page.getRecords();

        if (records == null || records.isEmpty()) {
            return new PageUtils(page);
        }

        // 分类ids
        List<Long> catelogIds = records.stream().map(AttrEntity::getCatelogId).distinct().collect(Collectors.toList());
        // 分组ids
        List<Long> attrIds = records.stream().map(AttrEntity::getAttrId).distinct().collect(Collectors.toList());

        // List<Long> attrGroupIds = attrAttrgroupRelationService.listObjs(
        //         Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
        //                 .select(AttrAttrgroupRelationEntity::getAttrGroupId)
        //                 .in(AttrAttrgroupRelationEntity::getAttrId, attrIds)
        //         , attrGroupId -> (Long) attrGroupId
        // );
        List<AttrAttrgroupRelationEntity> relationList = attrAttrgroupRelationService.list(
                Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                        .in(AttrAttrgroupRelationEntity::getAttrId, attrIds)
        );

        List<Long> attrGroupIds = Optional.ofNullable(relationList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .map(AttrAttrgroupRelationEntity::getAttrGroupId)
                .collect(Collectors.toList());

        // 分组信息
        List<AttrGroupEntity> attrGroupList = attrGroupService.listByIds(attrGroupIds);
        // 分类信息
        List<CategoryEntity> categoryList = categoryService.listByIds(catelogIds);
        // 转map
        // 属性id和组id map
        Map<Long, Long> attrIdAttrGroupIdMap = Optional.ofNullable(relationList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toMap(AttrAttrgroupRelationEntity::getAttrId, AttrAttrgroupRelationEntity::getAttrGroupId));
        // 分组map
        Map<Long, String> attrGroupNameMap = Optional.ofNullable(attrGroupList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toMap(AttrGroupEntity::getAttrGroupId, AttrGroupEntity::getAttrGroupName));
        // 分类map
        Map<Long, String> categoryNameMap = Optional.ofNullable(categoryList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toMap(CategoryEntity::getCatId, CategoryEntity::getName));

        List<AttrVo> attrVoList = records.stream().map(attrEntity -> {
            AttrVo attrVo = new AttrVo();
            BeanUtils.copyProperties(attrEntity, attrVo);
            // 分组名称
            attrVo.setGroupName(attrGroupNameMap.get(attrIdAttrGroupIdMap.get(attrVo.getAttrId())));
            // 分类名称
            attrVo.setCatelogName(categoryNameMap.get(attrVo.getCatelogId()));
            return attrVo;
        }).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);

        pageUtils.setList(attrVoList);

        return pageUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDetail(AttrDto attrDto) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrDto, attrEntity);
        this.save(attrEntity);
        return attrAttrgroupRelationService.save(AttrAttrgroupRelationEntity.builder()
                .attrId(attrDto.getAttrId())
                .attrGroupId(attrDto.getAttrGroupId())
                .build());
    }

    @Override
    public AttrVo getAttrDetail(Long attrId) {

        AttrEntity attrEntity = this.getById(attrId);
        AttrVo attrVo = new AttrVo();
        BeanUtils.copyProperties(attrEntity, attrVo);

        CategoryEntity categoryEntity = categoryService.getById(attrVo.getCatelogId());
        if (categoryEntity != null) {
            attrVo.setCatelogName(categoryEntity.getName());
        }

        List<Long> catelogPath = categoryService.findCatelogPath(attrVo.getCatelogId());
        attrVo.setCatelogPath(catelogPath);

        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getOne(
                Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrVo.getAttrId())
        );
        if (relationEntity != null) {
            AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
            if (groupEntity != null) {
                attrVo.setGroupName(groupEntity.getAttrGroupName());
            }
        }

        return attrVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDetail(AttrDto attrDto) {
        // 先保存关系的属性组
        attrAttrgroupRelationService.update(AttrAttrgroupRelationEntity.builder()
                        .attrId(attrDto.getAttrId())
                        .attrGroupId(attrDto.getAttrGroupId())
                        .build(),
                Wrappers.lambdaUpdate(AttrAttrgroupRelationEntity.class)
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrDto.getAttrId()));

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrDto, attrEntity);
        // 再保存基本信息
        return this.save(attrEntity);
    }
}