package com.feng.aihelper.aihelper.rag;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.zhipuai.autoconfigure.ZhiPuAiEmbeddingProperties;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingModel;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: txf
 * @Date: 2026/2/26
 */
@Configuration
public class VectorStoreConfig {

    private String zhipuApikey;

    public VectorStoreConfig() {
        String zhipuEnv = System.getenv("ZHIPU_ENV");
        if (zhipuEnv == null) {
            throw new RuntimeException("请设置环境变量 ZHIPU_ENV");
        }
        this.zhipuApikey = zhipuEnv;
    }

    // 配置一个简单的向量模型
    @Bean(name = "myZhipuEmbeddingModel")
    public EmbeddingModel embeddingModel() {
        ZhiPuAiApi aiApi = ZhiPuAiApi.builder()
                .apiKey(zhipuApikey)
                .build();
        return new ZhiPuAiEmbeddingModel(aiApi, MetadataMode.EMBED);
    }
    // 配置一个简单的向量存储
    @Bean("zhipuSimpleVectorStore")
    public VectorStore vectorStore(EmbeddingModel myZhipuEmbeddingModel) {
        return SimpleVectorStore.builder(myZhipuEmbeddingModel)
                .build();
    }
}
