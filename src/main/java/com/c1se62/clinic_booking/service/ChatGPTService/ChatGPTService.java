package com.c1se62.clinic_booking.service.ChatGPTService;

import com.c1se62.clinic_booking.dto.request.ChatRequest;
import com.c1se62.clinic_booking.dto.response.ApiResponse;
import com.c1se62.clinic_booking.dto.response.ChataiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ChatGPTService {
    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public ChatGPTService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public ChataiResponse getChatbotResponse(String userMessage) {
        // Tạo requestBody theo định dạng yêu cầu của API
        String requestBody = String.format("""
                {
                    "contents": [{
                        "parts": [{
                            "text": "%s"
                        }]
                    }]
                }
                """, userMessage);

        ChataiResponse chataiResponse = null;
        try {
            // Gửi yêu cầu tới API
            ApiResponse apiResponse = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(ApiResponse.class) // Ánh xạ trực tiếp vào ApiResponse
                    .block();
            chataiResponse = new ChataiResponse();
            if (apiResponse != null && apiResponse.getCandidates() != null && !apiResponse.getCandidates().isEmpty()) {
                ApiResponse.Candidate candidate = apiResponse.getCandidates().get(0);
                if (candidate.getContent() != null && candidate.getContent().getParts() != null && !candidate.getContent().getParts().isEmpty()) {
                    chataiResponse.setMessage(candidate.getContent().getParts().get(0).getText());
                    return chataiResponse;
                }
            }
            chataiResponse.setMessage("Không có câu trả lời từ chatbot.");
            return chataiResponse; // Trả về thông báo nếu không có kết quả.

        } catch (WebClientResponseException e) {
            return chataiResponse;// Xử lý lỗi WebClient
        } catch (Exception e) {
            return chataiResponse;
        }
    }
}
