package com.feng.aihelper.aihelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.feng.aihelper.aihelper.mapper")
public class AiHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiHelperApplication.class, args);
    }
}
