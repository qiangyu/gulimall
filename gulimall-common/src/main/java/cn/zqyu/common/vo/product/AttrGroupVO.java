package cn.zqyu.common.vo.product;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性分组
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
@Data
public class AttrGroupVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 该分组对应 一级到 catelogId 菜单 的完整路径
     */
    private List<Long> catelogPath;

}
