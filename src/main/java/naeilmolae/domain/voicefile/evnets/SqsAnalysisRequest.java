package naeilmolae.domain.voicefile.evnets;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.request.AnalysisRequestDto;
import naeilmolae.domain.voicefile.service.VoiceFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class SqsAnalysisRequest implements VoiceFileEventListener{
    private final VoiceFileService voiceFileService;
    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${kafka.topic.analysis.request}")
    private String queueName;

    @Override
    @EventListener
    @Transactional
    public void handleEvent(VoiceFileAnalysisEvent event) {
        log.info("VoiceFileAnalysisEvent received: {}", event.voiceFileId());
        VoiceFile voiceFile = voiceFileService.findById(event.voiceFileId());
        voiceFile.prepareAnalysis();

        AnalysisRequestDto analysisRequestDto = new AnalysisRequestDto(
                event.voiceFileId(),
                event.fileUrl(),
                event.content()
        );

        try {
            queueMessagingTemplate.convertAndSend(queueName, analysisRequestDto);
            log.info("Sent message via SQS: {}", analysisRequestDto);
        } catch (Exception e) {
            log.error("Failed to send message via SQS", e);
        }
    }
}
