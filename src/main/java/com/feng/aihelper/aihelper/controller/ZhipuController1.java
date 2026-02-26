package com.feng.aihelper.aihelper.controller;

import com.feng.aihelper.aihelper.tools.BookQueryTool;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: txf
 * @Date: 2026/2/23
 */
@RestController
@CrossOrigin
@RequestMapping("/zhipu1")
public class ZhipuController1 {

    @Resource(name = "myZhipu")
    private ZhiPuAiChatModel myZhipu; // 基于ChatModel形式的对话
    @Resource(name = "zhipuClient")
    private ChatClient zhipuClient; // 基于ChatClient形式的对话
    @Resource(name = "zhipuSimpleVectorStore")
    private VectorStore vectorStore; // 基于智普的简单向量存储

    @Resource(name = "inMemoryChatMemory")
    private ChatMemory inMemoryChatMemory;

    @GetMapping("/chat1")
    public String chat1(@RequestParam("message") String message) {
        return myZhipu.call(message);
    }

    @GetMapping(path = "/chat2Stream", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> chat2Stream(@RequestParam("message") String message) {
        return myZhipu.stream(message);
    }

    // // message: 请你描述一下这个图片！
    @GetMapping(path = "/chatMultimodality", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> chatMultimodality(@RequestParam("message") String message) {
        var resource = new ClassPathResource("/cqupt.png");
        UserMessage userMessage = UserMessage.builder().text(message)
                .media(new Media(MimeTypeUtils.IMAGE_PNG, resource))
                .build();
        return myZhipu.stream(userMessage);
    }

    // 有会话记忆的聊天
    @GetMapping(path = "/chatMemory", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> chatMemory(@RequestParam("sessionId") String sessionId,
                                   @RequestParam("message") String message) {
        return zhipuClient.prompt()
                .user(message)
                // 1. 显式指定要启用的工具方法名
                .toolNames("queryBookCount", "findBooksByAuthor")
                .advisors( spec ->
                     spec.advisors(QuestionAnswerAdvisor.builder(vectorStore).build()) // RAG
                         .param(ChatMemory.CONVERSATION_ID, sessionId) // 记忆参数
                )
                .stream().content();

        // 基于chatModel形式的对话，根据官网手动维护对话记忆
        // 1.构建用户消息
        // UserMessage userMessage = UserMessage.builder().text(message).build();
        // List<Message> messages = new ArrayList<>(inMemoryChatMemory.get(sessionId));
        // messages.add(userMessage);
        //
        // // 2.构建模型的返回数据
        // StringBuilder aiRes = new StringBuilder();
        //
        // Prompt prompt = new Prompt(messages);
        // return myZhipu.stream(prompt)
        //         .mapNotNull(res -> res.getResult().getOutput().getText())
        //         .doOnNext(aiRes::append)
        //         .doOnComplete(() -> {
        //             // 流结束时，构造助手消息并保存
        //             AssistantMessage assistantMessage = new AssistantMessage(aiRes.toString());
        //             // 保存用户消息和助手消息
        //             inMemoryChatMemory.add(sessionId, List.of(userMessage, assistantMessage));
        //         });
    }
}
