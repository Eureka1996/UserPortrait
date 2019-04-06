package com.wufuqiang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ author wufuqiang
 * @ date 2019/4/5/005 - 16:03
 **/

@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
public class Startupmain {
    public static void main(String[] args) {

        SpringApplication.run(Startupmain.class, args);
    }
}
