package cn.zqyu.gulimall.ware.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.utils.Query;
import cn.zqyu.gulimall.ware.dao.PurchaseDetailDao;
import cn.zqyu.gulimall.ware.entity.PurchaseDetailEntity;
import cn.zqyu.gulimall.ware.service.PurchaseDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseDetailEntity> queryWrapper = Wrappers.lambdaQuery(PurchaseDetailEntity.class);

        String key = (String) params.get("key");
        if (StrUtil.isNotBlank(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(PurchaseDetailEntity::getPurchaseId, key).or().eq(PurchaseDetailEntity::getSkuId, key);
            });
        }
        String status = (String) params.get("status");
        if (StrUtil.isNotBlank(status)) {
            queryWrapper.eq(PurchaseDetailEntity::getStatus, status);
        }
        String wareId = (String) params.get("wareId");
        if (StrUtil.isNotBlank(wareId) && !StrUtil.equals("0", wareId)) {
            queryWrapper.eq(PurchaseDetailEntity::getWareId, wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}