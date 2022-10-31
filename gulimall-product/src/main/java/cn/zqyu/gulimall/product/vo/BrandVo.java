package cn.zqyu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 品牌
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 20:31:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名
     */
    private String brandName;

}
