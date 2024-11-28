package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.ChatRequest;
import com.c1se62.clinic_booking.service.ChatGPTService.ChatGPTService;
import com.c1se62.clinic_booking.service.ChatGPTService.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
public class ChatGPTController {
    @Autowired
    private ChatGPTService chatService;
    @Autowired
    private SpeechToTextService speechToTextService;
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(
            @RequestBody(required = false) ChatRequest chatRequest,
            @RequestParam(value = "audioUrl", required = false) String audioUrl) {

        try {
            String userMessage;

            if (audioUrl != null && !audioUrl.isEmpty()) {
                // Chuyển giọng nói thành văn bản
                userMessage = speechToTextService.transcribeAudio(audioUrl);
            } else if (chatRequest != null && chatRequest.getUserMessage() != null) {
                // Lấy nội dung từ bàn phím
                userMessage = chatRequest.getUserMessage();
            } else {
                return ResponseEntity.badRequest().body("Không có dữ liệu để xử lý.");
            }

            // Gửi tin nhắn đến chatbot
            String chatbotResponse = chatService.getChatbotResponse(userMessage);
            return ResponseEntity.ok(chatbotResponse);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }
    }


}
