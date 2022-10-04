package cn.zqyu.gulimall.product.entity;

import cn.zqyu.common.valid.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(message = "修改必须指定品牌id", groups = UpdateGroup.class)
    @Null(message = "新增不用指定品牌id", groups = AddGroup.class)
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空", groups = {AllValidGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotBlank(message = "品牌logo地址不能为空", groups = AddGroup.class)
    @URL(message = "品牌logo必须是一个合法的url地址", groups = {AllValidGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = {AllValidGroup.class, UpdateStatusGroup.class})
    @ListValue(vals = {0, 1}, groups = {AllValidGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotBlank(message = "检索首字母不能为空", groups = AddGroup.class)
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须为a-zA-Z，且为一个字母", groups = {AllValidGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = AddGroup.class)
    @Min(value = 0, message = "排序必须大于0", groups = {AllValidGroup.class})
    private Integer sort;

}
