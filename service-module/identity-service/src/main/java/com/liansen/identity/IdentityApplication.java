package com.liansen.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 人员服务开启类
 * @author cdy
 * @create 2018/9/5
 */
@SpringBootApplication(scanBasePackages = "com.liansen")
public class IdentityApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdentityApplication.class,args);}
}
