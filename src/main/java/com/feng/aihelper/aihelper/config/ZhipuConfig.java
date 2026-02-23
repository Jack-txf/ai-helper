package com.feng.aihelper.aihelper.config;

import org.springframework.context.annotation.Configuration;

/**
 * @Description: 智谱清言AI
 * @Author: txf
 * @Date: 2026/2/23
 */
@Configuration
public class ZhipuConfig {

    private String zhipuApikey;

    public ZhipuConfig() {
        String zhipuEnv = System.getenv("ZHIPU_ENV");
        if (zhipuEnv == null) {
            throw new RuntimeException("请设置环境变量 ZHIPU_ENV");
        }
        this.zhipuApikey = zhipuEnv;
    }

    public String getZhipuApikey() {
        return zhipuApikey;
    }
}
