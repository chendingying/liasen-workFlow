package com.liansen.form;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 表单接口
 * @author cdy
 * @create 2018/9/5
 */
@SpringBootApplication(scanBasePackages = "com.liansen")
public class FormApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormApplication.class,args);
    }
}
