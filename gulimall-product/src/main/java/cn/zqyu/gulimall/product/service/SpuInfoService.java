package cn.zqyu.gulimall.product.service;

import cn.zqyu.common.vo.product.SpuSaveVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * <p>
     * 保存spu的详细信息：里面包括基本属性信息、sku销售属性信息等等
     *
     * </p>
     *
     * @param spuInfoVo spuInfoVo   /
     * @author zq yu
     * @since 2022/10/31 15:24
     */
    void saveDetailInfo(SpuSaveVO spuInfoVo);
}

