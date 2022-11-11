package cn.zqyu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:59:49
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * <p>
     * 批量添加库存
     *
     * </p>
     *
     * @param wareSkuList wareSkuList
     * @return boolean /
     * @author zq yu 
     * @since 2022/11/11 10:19
     */
    boolean addStockBatch(List<WareSkuEntity> wareSkuList);
}

