package com.feng.aihelper.aihelper.config;

import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.context.annotation.Bean;
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

    @Bean(name = "myZhipu")
    public ZhiPuAiChatModel zhiPuAiChatModel() {
        ZhiPuAiApi aiApi = ZhiPuAiApi.builder()
                .apiKey(zhipuApikey)
                .build();
        return new ZhiPuAiChatModel(
                aiApi,
                ZhiPuAiChatOptions.builder().model("glm-4.6v")
                        .build()
        );
    }


}
