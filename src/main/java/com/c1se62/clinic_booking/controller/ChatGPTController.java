package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.ChatRequest;
import com.c1se62.clinic_booking.dto.response.ChataiResponse;
import com.c1se62.clinic_booking.service.ChatGPTService.ChatGPTService;
import com.c1se62.clinic_booking.service.ChatGPTService.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ChataiResponse> askQuestion(
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
            @RequestBody(required = false) ChatRequest chatRequest) {

        try {
            String message; // Changed to lowercase for consistency

            if (audioFile != null && !audioFile.isEmpty()) {
                message = speechToTextService.transcribeAudio(audioFile);
            } else if (chatRequest != null && chatRequest.getUserMessage() != null) {
                message = chatRequest.getUserMessage();
            } else {
                return ResponseEntity.badRequest().body(new ChataiResponse("No data to process.")); //Improved response
            }

            String userMessage = "Giả sử bạn là một chatai chuyên gia về y tế hãy tư vấng bệnh cho tôi nhớ là câu trả lời phải thật ngắn gọn" + message;
            ChataiResponse chatbotResponse = chatService.getChatbotResponse(userMessage);
            return ResponseEntity.ok(chatbotResponse); // Wrap in ResponseEntity

        } catch (IOException e) {
            //More specific error handling
            String errorMessage = "Error processing audio file: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ChataiResponse(errorMessage)); //Improved response
        } catch (Exception e) {
            //Log the exception for debugging purposes
            e.printStackTrace(); //Important for debugging, remove in production
            String errorMessage = "An unexpected error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ChataiResponse(errorMessage)); //Improved response

        }
}
}