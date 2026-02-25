package com.feng.aihelper.aihelper.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
}
