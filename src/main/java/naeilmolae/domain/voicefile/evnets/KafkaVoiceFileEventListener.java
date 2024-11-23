package naeilmolae.domain.voicefile.evnets;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.request.AnalysisRequestDto;
import naeilmolae.domain.voicefile.dto.response.AnalysisResponseDto;
import naeilmolae.domain.voicefile.service.VoiceFileService;
import naeilmolae.global.util.KafkaTopic;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaVoiceFileEventListener implements VoiceFileEventListener {

    private final KafkaTemplate<String, AnalysisRequestDto> kafkaTemplate;
    private final VoiceFileService voiceFileService;

    @Override
    @EventListener
    @Transactional
    public void handleEvent(VoiceFileAnalysisEvent event) {
        VoiceFile voiceFile = voiceFileService.findById(event.voiceFileId());
        voiceFile.prepareAnalysis();
        AnalysisRequestDto analysisRequestDto = new AnalysisRequestDto(event.fileUrl(), event.content());
//        kafkaTemplate.send(KafkaTopic.ANALYSIS_REQUEST, event.voiceFileId().toString(), analysisRequestDto);
        kafkaTemplate.setDefaultTopic(KafkaTopic.ANALYSIS_REQUEST);
        kafkaTemplate.send(MessageBuilder.withPayload(analysisRequestDto)
                .setHeader(KafkaHeaders.KEY, event.voiceFileId().toString())
                .setHeader("requiredResponseType", AnalysisResponseDto.class.getName())
                .build());
    }

}