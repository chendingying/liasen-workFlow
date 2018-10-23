package com.liansen.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 *
 * 流程引擎服务类
 * @author cdy
 * @create 2018/9/4
 */
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.liansen")
public class FlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class, args);
    }
}
