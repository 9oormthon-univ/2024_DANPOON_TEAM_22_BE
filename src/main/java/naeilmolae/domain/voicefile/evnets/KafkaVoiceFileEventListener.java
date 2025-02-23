package naeilmolae.domain.voicefile.evnets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.request.AnalysisRequestDto;
import naeilmolae.domain.voicefile.dto.response.AnalysisResponseDto;
import naeilmolae.domain.voicefile.service.VoiceFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaVoiceFileEventListener implements VoiceFileEventListener {

    private final KafkaTemplate<String, AnalysisRequestDto> kafkaTemplate;
    private final VoiceFileService voiceFileService;

    @Value("${kafka.topic.analysis.request}")
    private String TOPIC;

    @Override
    @EventListener
    @Transactional
    public void handleEvent(VoiceFileAnalysisEvent event) {
        log.info("VoiceFileAnalysisEvent received: {}", event.voiceFileId());
        VoiceFile voiceFile = voiceFileService.findById(event.voiceFileId());
        voiceFile.prepareAnalysis();
        AnalysisRequestDto analysisRequestDto = new AnalysisRequestDto(event.fileUrl(), event.content());

        kafkaTemplate.setDefaultTopic(TOPIC);
        CompletableFuture<SendResult<String, AnalysisRequestDto>> future = kafkaTemplate.send(MessageBuilder.withPayload(analysisRequestDto)
                .setHeader(KafkaHeaders.KEY, event.voiceFileId().toString())
                .setHeader("requiredResponseType", AnalysisResponseDto.class.getName())
                .build());

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message to Kafka: {}", ex.getMessage());
            } else {
                log.info("Sent message to Kafka: {}", result.getProducerRecord().value());
            }
        });
    }
}
