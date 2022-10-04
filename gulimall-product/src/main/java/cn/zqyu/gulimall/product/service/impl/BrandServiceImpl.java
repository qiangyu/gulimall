package cn.zqyu.gulimall.product.service.impl;

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

import cn.zqyu.gulimall.product.dao.BrandDao;
import cn.zqyu.gulimall.product.entity.BrandEntity;
import cn.zqyu.gulimall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        final String name = (String) params.get("key");
        LambdaQueryWrapper<BrandEntity> lambdaQuery = Wrappers.lambdaQuery(BrandEntity.class);
        if (StrUtil.isNotBlank(name)) {
            lambdaQuery.likeRight(BrandEntity::getName, name);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                lambdaQuery
        );

        return new PageUtils(page);
    }

}