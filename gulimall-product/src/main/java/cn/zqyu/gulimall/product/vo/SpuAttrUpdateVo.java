package cn.zqyu.gulimall.product.vo;

import lombok.Data;

/**
 * <p>
 * 接收修改spu规格参数的vo
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.product.vo
 * @see SpuAttrUpdateVo
 * @since 2022/11/12 23:26
 */
@Data
public class SpuAttrUpdateVo {

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 属性值
     */
    private String attrValue;
    /**
     * 顺序
     */
    private Integer attrSort;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】
     */
    private Integer quickShow;

}
