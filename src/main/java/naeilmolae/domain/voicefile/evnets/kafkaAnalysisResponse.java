package naeilmolae.domain.voicefile.evnets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.chatgpt.dto.ScriptValidationResponseDto;
import naeilmolae.domain.chatgpt.service.ChatGptService;
import naeilmolae.domain.voicefile.domain.AnalysisResultStatus;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.response.AnalysisResponseDto;
import naeilmolae.domain.voicefile.service.VoiceFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!prod")
public class kafkaAnalysisResponse {

    private final VoiceFileService voiceFileService;
    private final ChatGptService chatGptService;

    /**
     * Spring Cloud Stream 함수형 모델을 사용하여 메시지를 소비합니다.
     * 메시지 헤더에서 Kafka의 key("kafka_receivedMessageKey")를 추출하여 사용합니다.
     */
    @Bean
    public Consumer<Message<AnalysisResponseDto>> analysisResponseConsumer() {
        return message -> {
            AnalysisResponseDto analysisResponseDto = message.getPayload();
            Long voiceFileId = analysisResponseDto.voiceFileId();
            VoiceFile voiceFile = voiceFileService.findById(voiceFileId);

            if ("SUCCESS".equals(analysisResponseDto.analysisResultStatus())) {
                ScriptValidationResponseDto scriptValidationResponseDto =
                        chatGptService.getCheckScriptRelevancePrompt2(voiceFile.getContent(), analysisResponseDto.sttContent());

                log.info("음성 유사도 분석 결과 : {}", scriptValidationResponseDto);

                if (!scriptValidationResponseDto.isProper()) {
                    Integer reason = scriptValidationResponseDto.getReason();
                    AnalysisResultStatus analysisResultStatus = AnalysisResultStatus.of(reason);
                    String description = analysisResultStatus.getDescription();
                    // 새로운 AnalysisResponseDto 생성 시 voiceFileId도 포함시킵니다.
                    voiceFileService.saveResult(
                            new AnalysisResponseDto(voiceFileId, description, analysisResponseDto.sttContent()));
                    log.info("음성 유사도 분석 실패 : {}", description);
                } else {
                    voiceFileService.saveResult(analysisResponseDto);
                }
            } else {
                // 실패 저장
                log.error("stt 서버에서 에러 발생");
                voiceFileService.saveResult(analysisResponseDto);
            }
        };
    }
}
