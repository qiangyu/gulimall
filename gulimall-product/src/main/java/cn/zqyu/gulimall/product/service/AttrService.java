package cn.zqyu.gulimall.product.service;

import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.product.vo.AttrVo;
import cn.zqyu.gulimall.product.entity.AttrEntity;
import cn.zqyu.gulimall.product.vo.AttrRespVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 商品属性
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId);

    boolean saveDetail(AttrVo attrVo);

    /**
     * <p>
     * 获取属性详细信息，包括其分组
     *
     * </p>
     *
     * @param attrId attrId
     * @return cn.zqyu.gulimall.product.vo.AttrVo /
     * @author zq yu
     * @since 2022/10/13 23:12
     */
    AttrRespVo getAttrDetail(Long attrId);

    /**
     * <p>
     * 更新属性详细信息，包括其分组
     *
     * </p>
     *
     * @param attrVo attrDto
     * @return boolean /
     * @author zq yu
     * @since 2022/10/13 23:19
     */
    boolean updateDetail(AttrVo attrVo);
}

