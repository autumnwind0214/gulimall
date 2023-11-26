package com.example.gulimall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Autowired
    OSSClient ossClient;

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
    void contextLoads() {
    }

}
