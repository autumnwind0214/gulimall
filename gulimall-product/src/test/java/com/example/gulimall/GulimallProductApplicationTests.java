package com.example.gulimall;

import com.example.gulimall.product.entity.BrandEntity;
import com.example.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {

    // @Value("${spring.cloud.alicloud.access-key}")
    private String accessKey;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
    }

    @Test
    void test() {
        System.out.println(accessKey);
        System.out.println(secretKey);
        System.out.println(endpoint);
        System.out.println(bucket);
    }

}
