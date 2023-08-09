package com.gettogetherbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGetTogetherBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(GetTogetherBackendApplication::main).with(TestGetTogetherBackendApplication.class).run(args);
    }

}
