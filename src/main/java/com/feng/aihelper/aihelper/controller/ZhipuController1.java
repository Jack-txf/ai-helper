package com.feng.aihelper.aihelper.controller;

import com.feng.aihelper.aihelper.tools.BookQueryTool;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
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
    private ZhiPuAiChatModel myZhipu;
    @Resource(name = "zhipuClient")
    private ChatClient zhipuClient;

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
                .advisors( spec -> spec.param(ChatMemory.CONVERSATION_ID, sessionId))
                .stream().content();

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
