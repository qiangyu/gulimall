package cn.zqyu.gulimall.product.service;

import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.product.entity.AttrEntity;
import cn.zqyu.gulimall.product.vo.AttrGroupWithAttrsVo;
import cn.zqyu.gulimall.product.vo.AttrRespVo;
import cn.zqyu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType);

    /**
     * <p>
     * 根据分类id查询出分组已经分组关联的属性
     *
     * </p>
     *
     * @param catlogId catlogId
     * @return java.util.List<cn.zqyu.gulimall.product.vo.AttrGroupWithAttrsVo> /
     * @author zq yu
     * @since 2022/10/29 17:01
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatlogId(Long catlogId);

    /**
     * <p>
     * 查询当前分组关联的所有基本属性
     *
     * </p>
     *
     * @param attrgroupId attrgroupId
     * @return java.util.List<cn.zqyu.gulimall.product.entity.AttrEntity> /
     * @author zq yu
     * @since 2022/10/29 17:15
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    /**
     * <p>
     * 保存信息并且添加关联关系
     *
     * </p>
     *
     * @param attrVo attrVo
     * @return boolean /
     * @author zq yu
     * @since 2022/10/29 17:14
     */
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

    /**
     * <p>
     * 获取关联分组的属性
     *
     * </p>
     *
     * @param attrgroupId attrgroupId
     * @return java.util.List<cn.zqyu.gulimall.product.entity.AttrEntity> /
     * @author zq yu
     * @since 2022/11/1 17:10
     */
    List<AttrEntity> getAttrRelation(Long attrgroupId);

    /**
     * <p>
     * 获取该分组对应分类下，还没关联的属性
     *
     * </p>
     *
     * @param params params
     * @param attrgroupId attrgroupId
     * @return cn.zqyu.common.utils.PageUtils /
     * @author zq yu
     * @since 2022/11/1 17:22
     */
    PageUtils getAttrNoRelation(Map<String, Object> params, Long attrgroupId);
}

