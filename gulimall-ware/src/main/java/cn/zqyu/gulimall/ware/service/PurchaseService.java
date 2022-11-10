package cn.zqyu.gulimall.ware.service;

import cn.zqyu.gulimall.ware.vo.MergePurchaseItemVo;
import cn.zqyu.gulimall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.zqyu.common.utils.PageUtils;
import cn.zqyu.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购单
 *
 * @author zq yu
 * @email me@zqyu.cn
 * @date 2022-09-21 21:59:49
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * <p>
     * 获取新建和已分配状态的采购单
     *
     * </p>
     *
     * @param params params
     * @return cn.zqyu.common.utils.PageUtils /
     * @author zq yu
     * @since 2022/11/10 15:54
     */
    PageUtils queryPageUnReceiveList(Map<String, Object> params);

    /**
     * <p>
     * 将采购需求合并整到采购单
     *      当传入的采购单为null，则创建新的采购单
     *
     * </p>
     *
     * @param mergePurchaseItemVo mergePurchaseItemVo
     * @return int  返回是否改变成功
     * @author zq yu
     * @since 2022/11/10 16:09
     */
    boolean mergePurchaseItem(MergePurchaseItemVo mergePurchaseItemVo);

    /**
     * <p>
     * 批量领取采购单
     *
     * </p>
     *
     * @param purchaseIds purchaseIds
     * @return boolean /
     * @author zq yu
     * @since 2022/11/10 17:21
     */
    boolean received(List<Long> purchaseIds);

    /**
     * <p>
     * 完成采购单的采购
     *
     * </p>
     *
     * @param purchaseDoneVo purchaseDoneVo
     * @return boolean /
     * @author zq yu
     * @since 2022/11/10 17:48
     */
    boolean purchaseDone(PurchaseDoneVo purchaseDoneVo);
}

