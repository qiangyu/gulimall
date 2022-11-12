package cn.zqyu.gulimall.product.service.impl;

import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.product.dao.ProductAttrValueDao;
import cn.zqyu.gulimall.product.entity.ProductAttrValueEntity;
import cn.zqyu.gulimall.product.service.ProductAttrValueService;
import cn.zqyu.gulimall.product.vo.SpuAttrUpdateVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> getAttrListForSpu(Long spuId) {
        if (spuId == null) {
            throw new IllegalArgumentException("spu id is null");
        }

        return this.list(
                Wrappers.lambdaQuery(ProductAttrValueEntity.class)
                        .eq(ProductAttrValueEntity::getSpuId, spuId)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSpuAttr(final Long spuId, List<SpuAttrUpdateVo> updateAttrVoList) {
        if (spuId == null) {
            throw new IllegalArgumentException("spu id is null");
        }
        if (updateAttrVoList == null || updateAttrVoList.isEmpty()) {
            return false;
        }
        // 获取传递过来的attr id
        List<Long> attrIds = updateAttrVoList.stream().map(SpuAttrUpdateVo::getAttrId).collect(Collectors.toList());

        // 根据attr id查询表id
        List<ProductAttrValueEntity> dbAttrValueList = this.list(
                Wrappers.lambdaQuery(ProductAttrValueEntity.class)
                        .select(ProductAttrValueEntity::getId, ProductAttrValueEntity::getAttrId)
                        .eq(ProductAttrValueEntity::getSpuId, spuId)
                        .in(ProductAttrValueEntity::getAttrId, attrIds)
        );
        Map<Long, Long> attrIdMap = Optional.ofNullable(dbAttrValueList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toMap(ProductAttrValueEntity::getAttrId, ProductAttrValueEntity::getId));

        // 过滤 SpuAttrUpdateVo，存在其id则进行批量：修改
        List<ProductAttrValueEntity> updateAttrCollect = updateAttrVoList.stream()
                .filter(item -> attrIdMap.containsKey(item.getAttrId()))
                .map(item -> {
                    ProductAttrValueEntity entity = new ProductAttrValueEntity();
                    BeanUtils.copyProperties(item, entity);
                    entity.setSpuId(spuId);
                    entity.setId(attrIdMap.get(entity.getAttrId()));
                    return entity;
                })
                .collect(Collectors.toList());
        if (!updateAttrCollect.isEmpty()) {
            this.updateBatchById(updateAttrCollect);
        }

        // 过滤 SpuAttrUpdateVo，不存在其id则进行批量：插入
        List<ProductAttrValueEntity> insertAttrCollect = updateAttrVoList.stream()
                .filter(item -> !attrIdMap.containsKey(item.getAttrId()))
                .map(item -> {
                    ProductAttrValueEntity entity = new ProductAttrValueEntity();
                    BeanUtils.copyProperties(item, entity);
                    entity.setSpuId(spuId);
                    return entity;
                })
                .collect(Collectors.toList());
        if (!insertAttrCollect.isEmpty()) {
            this.saveBatch(insertAttrCollect);
        }
        return true;
    }

}