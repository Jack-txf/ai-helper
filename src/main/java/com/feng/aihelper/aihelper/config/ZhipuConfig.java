package com.feng.aihelper.aihelper.config;

import com.feng.aihelper.aihelper.tools.BookQueryTool;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
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

    @Resource
    private BookQueryTool bookQueryTool;

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
                ZhiPuAiChatOptions.builder()
                        .model("glm-4.6v")
                        .build()
        );
    }

    @Bean("zhipuClient")
    public ChatClient chatClient(ZhiPuAiChatModel zhiPuAiChatModel,
                                 BookQueryTool bookQueryTool,
                                 ChatMemory chatMemory) {
        return ChatClient.builder(zhiPuAiChatModel)
                // 关键点 1：注册工具回调，ChatClient 会自动处理函数执行过程
                .defaultTools(bookQueryTool)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }


    // 基于内存的会话记忆
    @Bean("inMemoryChatMemory")
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
    }


}
