package cn.zqyu.gulimall.product.service;

import cn.zqyu.gulimall.product.vo.AttrGroupWithAttrsVo;
import cn.zqyu.gulimall.product.vo.AttrRelationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * <p>
     * 根据catelogId 及 params.get("key")(attrGroupName) 查询分组
     *
     * </p>
     *
     * @param params params
     * @param catelogId catelogId
     * @return cn.zqyu.common.utils.PageUtils /
     * @author zq yu
     * @since 2022/9/28 11:29
     */
    PageUtils queryPage(Map<String, Object> params, String catelogId);

    /**
     * <p>
     * 批量保存分组与属性的关联关系
     *
     * </p>
     *
     * @param attrRelationVos attrRelationVos
     * @return boolean /
     * @author zq yu
     * @since 2022/11/1 17:41
     */
    boolean saveBatchAttrRelation(AttrRelationVo[] attrRelationVos);

    boolean deleteBatchAttrRelation(AttrRelationVo[] attrRelationVos);
}

