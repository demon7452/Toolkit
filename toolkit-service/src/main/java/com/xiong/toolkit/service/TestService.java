package com.xiong.toolkit.service;

import com.xiong.toolkit.api.TestApi;
import org.springframework.stereotype.Service;

@Service
public class TestService implements TestApi {

    public void test(String name) {
        System.out.println("hello world " + name);

    }
}
