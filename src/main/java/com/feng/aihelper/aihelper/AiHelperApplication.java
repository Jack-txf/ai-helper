package com.feng.aihelper.aihelper;

import com.feng.aihelper.aihelper.config.ZhipuConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiHelperApplication {

    @Resource
    private ZhipuConfig zhipuConfig;

    public static void main(String[] args) {
        SpringApplication.run(AiHelperApplication.class, args);
    }
}
