package cn.zqyu.gulimall.ware.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;

import cn.zqyu.gulimall.ware.dao.WareSkuDao;
import cn.zqyu.gulimall.ware.entity.WareSkuEntity;
import cn.zqyu.gulimall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> queryWrapper = Wrappers.lambdaQuery(WareSkuEntity.class);

        String skuId = (String) params.get("skuId");
        if (StrUtil.isNotBlank(skuId) && !StrUtil.equals("0", skuId)) {
            queryWrapper.eq(WareSkuEntity::getSkuId, skuId);
        }
        String wareId = (String) params.get("wareId");
        if (StrUtil.isNotBlank(wareId) && !StrUtil.equals("0", wareId)) {
            queryWrapper.eq(WareSkuEntity::getWareId, wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}