package cn.zqyu.gulimall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.product.dao.AttrDao;
import cn.zqyu.gulimall.product.vo.AttrVo;
import cn.zqyu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import cn.zqyu.gulimall.product.entity.AttrEntity;
import cn.zqyu.gulimall.product.entity.AttrGroupEntity;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import cn.zqyu.gulimall.product.service.AttrAttrgroupRelationService;
import cn.zqyu.gulimall.product.service.AttrGroupService;
import cn.zqyu.gulimall.product.service.AttrService;
import cn.zqyu.gulimall.product.service.CategoryService;
import cn.zqyu.gulimall.product.vo.AttrRespVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

        List<AttrRespVo> attrRespVoList = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            // 分组名称
            attrRespVo.setGroupName(attrGroupNameMap.get(attrIdAttrGroupIdMap.get(attrRespVo.getAttrId())));
            // 分类名称
            attrRespVo.setCatelogName(categoryNameMap.get(attrRespVo.getCatelogId()));
            return attrRespVo;
        }).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);

        pageUtils.setList(attrRespVoList);

        return pageUtils;
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        // 根据分组id查询所有关联关系
        List<AttrAttrgroupRelationEntity> relationList = attrAttrgroupRelationService.
                list(Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class).eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId));
        // 封装基本属性id
        List<Long> attrIds = relationList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        // 根据id查询基本属性集合
        if (CollectionUtils.isEmpty(attrIds)) {
            return new ArrayList<>();
        }
        // 非空查询返回
        return this.listByIds(attrIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDetail(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.save(attrEntity);
        return attrAttrgroupRelationService.save(AttrAttrgroupRelationEntity.builder()
                .attrId(attrVo.getAttrId())
                .attrGroupId(attrVo.getAttrGroupId())
                .build());
    }

    @Override
    public AttrRespVo getAttrDetail(Long attrId) {

        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);

        CategoryEntity categoryEntity = categoryService.getById(attrRespVo.getCatelogId());
        if (categoryEntity != null) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }

        List<Long> catelogPath = categoryService.findCatelogPath(attrRespVo.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);

        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getOne(
                Wrappers.lambdaQuery(AttrAttrgroupRelationEntity.class)
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrRespVo.getAttrId())
        );
        if (relationEntity != null) {
            AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
            if (groupEntity != null) {
                attrRespVo.setGroupName(groupEntity.getAttrGroupName());
            }
        }

        return attrRespVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDetail(AttrVo attrVo) {
        // 先保存关系的属性组
        attrAttrgroupRelationService.update(AttrAttrgroupRelationEntity.builder()
                        .attrId(attrVo.getAttrId())
                        .attrGroupId(attrVo.getAttrGroupId())
                        .build(),
                Wrappers.lambdaUpdate(AttrAttrgroupRelationEntity.class)
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrVo.getAttrId()));

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        // 再保存基本信息
        return this.save(attrEntity);
    }
}