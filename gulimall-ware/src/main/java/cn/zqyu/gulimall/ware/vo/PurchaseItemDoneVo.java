package cn.zqyu.gulimall.ware.vo;

import lombok.Data;



@Data
public class PurchaseItemDoneVo {

    /**
     * 采购需求ID
     */
    private Long itemId;

    /**
     * 采购状态
     */
    private Integer status;

    /**
     * 如果采购，其原因
     */
    private String reason;

}
