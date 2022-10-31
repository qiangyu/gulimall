package cn.zqyu.gulimall.product.service;

import cn.zqyu.gulimall.product.vo.AttrGroupWithAttrsVo;
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
}

