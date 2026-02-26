package com.feng.aihelper.aihelper.rag;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @Description: 面试知识灌入service
 * @Author: txf
 * @Date: 2026/2/26
 */
@Service
public class InterviewKnowledgeService {

    @Resource(name = "zhipuSimpleVectorStore")
    private VectorStore vectorStore;

    // 定义持久化文件路径
    private static final String INDEX_FILE_PATH = "interview_index.json";

    public void initKnowledgeBase() {
        File file = new File(INDEX_FILE_PATH);
        if (file.exists()) {
            // 情况 A: 如果 JSON 文件已存在，直接从文件加载向量数据
            System.out.println("检测到本地向量索引文件，正在加载...");
            if ( vectorStore instanceof SimpleVectorStore simpleStore) {
                simpleStore.load(file);
            }
            System.out.println("向量索引加载完毕！");
        } else {
            ClassPathResource resource = new ClassPathResource("docs/a_面试宝库.md");
            // 1. 读取 MD 文件
            TikaDocumentReader reader = new TikaDocumentReader(resource);
            List<Document> documents = reader.get();
            // 2. 文本切分 (TokenTextSplitter)
            // 设为 500 Token 一个块，重叠 50 Token 防止知识点被切断
            TokenTextSplitter splitter = new TokenTextSplitter(500, 50, 5, 10000, true);
            List<Document> splitDocs = splitter.apply(documents);
            // 3. 存入向量库 (这里会自动调用 Embedding 模型)
            vectorStore.accept(splitDocs);
            // 如果是 SimpleVectorStore，可以持久化到本地
            if (vectorStore instanceof SimpleVectorStore simpleStore) {
                simpleStore.save(new File("interview_index.json"));
            }
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化面试知识库...");
        initKnowledgeBase();
    }

}
