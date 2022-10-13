package cn.zqyu.gulimall.product.service;

import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.common.vo.product.CategoryEntityVO;
import cn.zqyu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:20
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * <p>
     * 获取所有菜单的树形结构
     *
     * </p>
     *
     * @return java.util.List<cn.zqyu.common.vo.product.CategoryEntityVO> /
     * @author zq yu
     * @since 2022/9/23 11:42
     */
    List<CategoryEntityVO> getCategoryTree();

    /**
     * <p>
     * 根据 ids 逻辑删除分类
     *
     * </p>
     *
     * @param ids ids
     * @return java.util.List<cn.zqyu.gulimall.product.entity.CategoryEntity> 返回未成功删除分类信息
     * @author zq yu
     * @since 2022/9/24 13:49
     */
    List<CategoryEntity> removeMenuByIds(List<Long> ids);

    /**
     * <p>
     * 根据catelogId递归获取所有本身到一级菜单的id路径
     *
     * </p>
     *
     * @param catelogId catelogId
     * @return java.util.List<java.lang.Long> /
     * @author zq yu
     * @since 2022/9/29 12:12
     */
    List<Long> findCatelogPath(Long catelogId);
}

