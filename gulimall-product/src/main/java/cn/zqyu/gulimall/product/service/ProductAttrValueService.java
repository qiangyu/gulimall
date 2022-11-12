package cn.zqyu.gulimall.product.service;

import cn.zqyu.gulimall.product.vo.SpuAttrUpdateVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * <p>
     * 根据spuId获取其规格属性
     *
     * </p>
     *
     * @param spuId spuId
     * @return java.util.List<cn.zqyu.gulimall.product.entity.ProductAttrValueEntity> /
     * @author zq yu
     * @since 2022/11/12 23:19
     */
    List<ProductAttrValueEntity> getAttrListForSpu(Long spuId);

    /**
     * <p>
     * 根据spuId修改其规格属性
     *
     * </p>
     *
     * @param spuId spuId
     * @param updateAttrVoList updateAttrVoList
     * @return boolean /
     * @author zq yu
     * @since 2022/11/12 23:28
     */
    boolean updateSpuAttr(final Long spuId, List<SpuAttrUpdateVo> updateAttrVoList);

}

