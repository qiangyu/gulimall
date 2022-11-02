package cn.zqyu.gulimall.product;

import cn.zqyu.gulimall.product.entity.AttrEntity;
import cn.zqyu.gulimall.product.service.AttrService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class GulimallProductApplicationTests {

    @Resource
    AttrService attrService;

    @Test
    void contextLoads() {

        AttrEntity attrEntity = new AttrEntity();
        attrEntity.setAttrName("test");
        attrService.save(attrEntity);

        /*

        {"spuName":"苹果test","spuDescription":"苹果啊啊啊啊","catalogId":225,"brandId":12,"weight":1.7,"publishStatus":0,"decript":["https://gulimall---zch.oss-cn-qingdao.aliyuncs.com/2022-11-02/c991ca0a-f58a-4208-82a1-ffa928b8d32f_a2c208410ae84d1f.jpg"],"images":["https://gulimall---zch.oss-cn-qingdao.aliyuncs.com/2022-11-02/3383b981-9ffa-49ed-81fe-0412fb6516bc_ccd1077b985c7150.jpg"],"bounds":{"buyBounds":500,"growBounds":500},"baseAttrs":[{"attrId":7,"attrValues":"以官网信息为准","showDesc":0},{"attrId":8,"attrValues":"2019","showDesc":0},{"attrId":10,"attrValues":"4GB","showDesc":0},{"attrId":9,"attrValues":"黑色","showDesc":0},{"attrId":11,"attrValues":"黑色","showDesc":1},{"attrId":12,"attrValues":"12g","showDesc":0},{"attrId":13,"attrValues":"158.3","showDesc":0},{"attrId":14,"attrValues":"陶瓷;玻璃","showDesc":0},{"attrId":15,"attrValues":"以官网信息为准","showDesc":1},{"attrId":16,"attrValues":"骁龙845","showDesc":0}],"skus":[{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"白色"},{"attrId":10,"attrName":"内存","attrValue":"8GB"},{"attrId":12,"attrName":"版本","attrValue":"8+512"}],"skuName":"苹果test 白色 8GB 8+512","price":"1999","skuTitle":"苹果test 白色 8GB 8+512","skuSubtitle":"啊啊啊","images":[{"imgUrl":"","defaultImg":0}],"descar":["白色","8GB","8+512"],"fullCount":0,"discount":0,"countStatus":0,"fullPrice":0,"reducePrice":0,"priceStatus":0,"memberPrice":[{"id":2,"name":"铜牌会员","price":0},{"id":3,"name":"银牌会员","price":0}]},{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"白色"},{"attrId":10,"attrName":"内存","attrValue":"8GB"},{"attrId":12,"attrName":"版本","attrValue":"6+256"}],"skuName":"苹果test 白色 8GB 6+256","price":"1999","skuTitle":"苹果test 白色 8GB 6+256","skuSubtitle":"啊啊啊","images":[{"imgUrl":"","defaultImg":0}],"descar":["白色","8GB","6+256"],"fullCount":0,"discount":0,"countStatus":0,"fullPrice":0,"reducePrice":0,"priceStatus":0,"memberPrice":[{"id":2,"name":"铜牌会员","price":0},{"id":3,"name":"银牌会员","price":0}]},{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"白色"},{"attrId":10,"attrName":"内存","attrValue":"12GB"},{"attrId":12,"attrName":"版本","attrValue":"8+512"}],"skuName":"苹果test 白色 12GB 8+512","price":"1999","skuTitle":"苹果test 白色 12GB 8+512","skuSubtitle":"啊啊啊","images":[{"imgUrl":"","defaultImg":0}],"descar":["白色","12GB","8+512"],"fullCount":0,"discount":0,"countStatus":0,"fullPrice":0,"reducePrice":0,"priceStatus":0,"memberPrice":[{"id":2,"name":"铜牌会员","price":0},{"id":3,"name":"银牌会员","price":0}]},{"attr":[{"attrId":9,"attrName":"颜色","attrValue":"白色"},{"attrId":10,"attrName":"内存","attrValue":"12GB"},{"attrId":12,"attrName":"版本","attrValue":"6+256"}],"skuName":"苹果test 白色 12GB 6+256","price":"1999","skuTitle":"苹果test 白色 12GB 6+256","skuSubtitle":"啊啊啊","images":[{"imgUrl":"","defaultImg":0}],"descar":["白色","12GB","6+256"],"fullCount":0,"discount":0,"countStatus":0,"fullPrice":0,"reducePrice":0,"priceStatus":0,"memberPrice":[{"id":2,"name":"铜牌会员","price":0},{"id":3,"name":"银牌会员","price":0}]}]}


         */

    }

}
