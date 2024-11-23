package naeilmolae.domain.voicefile.evnets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.voicefile.dto.response.AnalysisResponseDto;
import naeilmolae.domain.voicefile.service.VoiceFileService;
import naeilmolae.global.util.KafkaTopic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoiceFileAnalysisResponseConsumer {

    private final VoiceFileService voiceFileService;

    // TODO consumer lag monitoring
    @KafkaListener(topics = KafkaTopic.ANALYSIS_RESPONSE, groupId = "voice-analysis-group1")
    public void handleAnalysisResponse(
            @Payload AnalysisResponseDto analysisResponseDto,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        // TODO 로깅 잘 해야함
        log.info("Received analysis response: {}", analysisResponseDto);
        voiceFileService.saveResult(Long.valueOf(key), analysisResponseDto);
    }
}