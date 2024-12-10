package com.c1se62.clinic_booking.service.ChatGPTService;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.*;
import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class SpeechToTextService {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextService.class);

    @Value("${assemblyai.api.key}")
    private String assemblyAiApiKey;

    public String transcribeAudio(MultipartFile audioFile) throws IOException {
        //1. Save the file to a temporary location
        File tempFile = saveToTemp(audioFile);

        //2. Get the file path and construct a file URL
        String audioFilePath = tempFile.getAbsolutePath();
        String audioUrl = "file://" + audioFilePath;

        try {
            // 3. Transcribe using the file URL
            String transcription = transcribeAudioFromURL(audioUrl);
            //4. Delete the temporary file
            Files.deleteIfExists(tempFile.toPath());
            return transcription;

        } catch (Exception e) {
            //Clean up even if transcription fails.
            Files.deleteIfExists(tempFile.toPath());
            logger.error("Error during transcription:", e);
            return "Error during transcription: " + e.getMessage();
        }

    }

    private File saveToTemp(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("audio", file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));
        Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return tempFile;

    }

    private String transcribeAudioFromURL(String audioUrl) {
        try {
            AssemblyAI client = AssemblyAI.builder()
                    .apiKey(assemblyAiApiKey)
                    .build();

            Transcript transcript = client.transcripts().transcribe(audioUrl);

            TranscriptStatus status = transcript.getStatus();
            if (status == TranscriptStatus.ERROR) {
                String errorMessage = transcript.getError().orElse("Unknown error");
                logger.error("AssemblyAI transcription failed: {}, audioUrl: {}", errorMessage, audioUrl);
                throw new RuntimeException("AssemblyAI transcription failed: " + errorMessage);
            } else if (status != TranscriptStatus.COMPLETED) {
                logger.warn("AssemblyAI transcription not completed yet, status: {}, audioUrl: {}", status, audioUrl);
                return "Transcription in progress. Check back later.";
            }

            return transcript.getText().orElse("No text received from audio.");
        } catch (RuntimeException e) {
            logger.error("Error during AssemblyAI transcription: ", e);
            return "Error during transcription: " + e.getMessage();
        } catch (Exception e) {
            logger.error("Unexpected error during transcription: ", e);
            return "An unexpected error occurred during transcription.";
        }
    }
}