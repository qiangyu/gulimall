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

    }

}
