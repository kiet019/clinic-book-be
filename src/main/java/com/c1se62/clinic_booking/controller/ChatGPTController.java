package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.ChatRequest;
import com.c1se62.clinic_booking.service.ChatGPTService.ChatGPTService;
import com.c1se62.clinic_booking.service.ChatGPTService.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/chatbot")
public class ChatGPTController {

    @Autowired
    private ChatGPTService chatService;
    @Autowired
    private SpeechToTextService speechToTextService;

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
            @RequestBody(required = false) ChatRequest chatRequest) {

        try {
            String userMessage;

            if (audioFile != null && !audioFile.isEmpty()) {
                userMessage = speechToTextService.transcribeAudio(audioFile);
            } else if (chatRequest != null && chatRequest.getUserMessage() != null) {
                userMessage = chatRequest.getUserMessage();
            } else {
                return ResponseEntity.badRequest().body("No data to process.");
            }

            String chatbotResponse = chatService.getChatbotResponse(userMessage);
            return ResponseEntity.ok(chatbotResponse);

        } catch (IOException e) {
            return ResponseEntity.status(400).body("Error processing audio file: " + e.getMessage()); // Handle IOException specifically
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}