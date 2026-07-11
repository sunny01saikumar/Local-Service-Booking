package com.sai.geoLocation.controller;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.entity.ChatMessage;
import com.sai.geoLocation.entity.User;
import com.sai.geoLocation.repository.UserRepository;
import com.sai.geoLocation.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository;

    public ChatController(ChatService chatService, UserRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    public record MessageRequest(
        String messageType,
        String content,
        String mediaUrl,
        BigDecimal latitude,
        BigDecimal longitude
    ) {}

    @PostMapping("/{bookingId}/send")
    public ApiResponse<ChatMessage> sendMessage(Principal principal, @PathVariable UUID bookingId, @RequestBody MessageRequest request) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        ChatMessage msg = chatService.sendMessage(
                bookingId,
                user.getId(),
                request.messageType(),
                request.content(),
                request.mediaUrl(),
                request.latitude(),
                request.longitude()
        );
        return ApiResponse.ok("Message sent", msg);
    }

    @GetMapping("/{bookingId}/history")
    public ApiResponse<List<ChatMessage>> getHistory(@PathVariable UUID bookingId) {
        return ApiResponse.ok("Fetched history", chatService.getHistory(bookingId));
    }

    @PostMapping("/{bookingId}/read")
    public ApiResponse<Void> markAsRead(Principal principal, @PathVariable UUID bookingId) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        chatService.markAsRead(bookingId, user.getId());
        return ApiResponse.ok("Messages marked as read", null);
    }
}
