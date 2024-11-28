package com.c1se62.clinic_booking.service.ChatGPTService;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.*;
import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SpeechToTextService {

    @Value("${assemblyai.api.key}")
    private String assemblyAiApiKey;

    public String transcribeAudio(String audioUrl) {
        try {
            // Khởi tạo client AssemblyAI
            AssemblyAI client = AssemblyAI.builder()
                    .apiKey(assemblyAiApiKey)
                    .build();

            // Thực hiện chuyển đổi giọng nói thành văn bản
            Transcript transcript = client.transcripts().transcribe(audioUrl);

            // Kiểm tra trạng thái của transcript
            if (transcript.getStatus() == TranscriptStatus.ERROR) {
                throw new Exception("Transcript failed with error: " + transcript.getError().orElse("Unknown error"));
            }

            return transcript.getText().orElse("Không nhận được văn bản từ âm thanh.");
        } catch (Exception e) {
            return "Lỗi trong quá trình chuyển đổi: " + e.getMessage();
        }
    }
}
