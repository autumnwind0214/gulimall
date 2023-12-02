package com.example.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKey;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Autowired
    OSS ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {


        // 上传文件流
        InputStream inputStream = new FileInputStream("C:\\Users\\longyan\\Pictures\\壁纸\\山脉 流云 唯美4k风景壁纸_彼岸图网.jpg");
        ossClient.putObject("gulimall-autuon", "彼岸图网11.jpg", inputStream);

        // 关闭ossclient
        ossClient.shutdown();
        System.out.println("上传成功");

    }

    @Test
    void test() {
        System.out.println(accessKey);
        System.out.println(secretKey);
        System.out.println(endpoint);
        System.out.println(bucket);
    }

}
