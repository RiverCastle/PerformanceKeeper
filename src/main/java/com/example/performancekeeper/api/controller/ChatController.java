package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.websocket.ChatHandler;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatHandler chatHandler;
    private final Gson gson;

    @GetMapping("/rooms")
    public String rooms() {
        return "common/chat/rooms";
    }

    @GetMapping("/enter")
    public String enter(@RequestParam("username") String username) {
        return "common/chat/chat";
    }
}
