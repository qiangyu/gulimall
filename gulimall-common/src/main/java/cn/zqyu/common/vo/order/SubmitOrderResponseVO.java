package cn.zqyu.common.vo.order;

import cn.zqyu.common.entity.order.OrderEntity;
import lombok.Data;

/**
 * 提交订单返回结果
 *
 * @author: wan
 */
@Data
public class SubmitOrderResponseVO {
    private OrderEntity order;
}
