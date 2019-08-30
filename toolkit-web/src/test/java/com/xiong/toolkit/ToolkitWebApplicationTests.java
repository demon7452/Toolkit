package com.xiong.toolkit;

import com.xiong.toolkit.api.TestApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToolkitWebApplicationTests {

    @Resource
    private TestApi testApi;

    @Value("${config.name}")
    private String name;

    @Test
    public void contextLoads() {

        testApi.test("xiong" + name);
    }

}
