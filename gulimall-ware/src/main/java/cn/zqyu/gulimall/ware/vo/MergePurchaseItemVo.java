package cn.zqyu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 合并采购项为采购单
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.ware.vo
 * @see MergePurchaseItemVo
 * @since 2022/11/10 16:01
 */
@Data
public class MergePurchaseItemVo {

    private Long purchaseId;
    private List<Long> items;

}
