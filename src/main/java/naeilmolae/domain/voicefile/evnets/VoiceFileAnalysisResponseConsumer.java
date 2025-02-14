package naeilmolae.domain.voicefile.evnets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.chatgpt.dto.ScriptValidationResponseDto;
import naeilmolae.domain.chatgpt.service.ChatGptService;
import naeilmolae.domain.voicefile.domain.AnalysisResultStatus;
import naeilmolae.domain.voicefile.domain.VoiceFile;
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
    private final ChatGptService chatGptService;

    /**
     * 음성 파일 분석 결과를 받아 GPT를 이용해 스크립트 유효성 검사를 진행한다.
     * @param analysisResponseDto
     * @param key
     */
    @KafkaListener(topics = KafkaTopic.ANALYSIS_RESPONSE, groupId = "voice-analysis-group1")
    public void handleAnalysisResponse(
            @Payload AnalysisResponseDto analysisResponseDto,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        VoiceFile voiceFile = voiceFileService.findById(Long.parseLong(key));

        if (analysisResponseDto.analysisResultStatus().equals("SUCCESS")) {
            ScriptValidationResponseDto scriptValidationResponseDto = chatGptService.getCheckScriptRelevancePrompt2(voiceFile.getContent(), analysisResponseDto.sttContent());
            if (!scriptValidationResponseDto.isProper()) {
                Integer reason = scriptValidationResponseDto.getReason();
                AnalysisResultStatus analysisResultStatus = AnalysisResultStatus.of(reason);
                String description = analysisResultStatus.getDescription();
                voiceFileService.saveResult(Long.parseLong(key), new AnalysisResponseDto(description, analysisResponseDto.sttContent()));
            } else {
                voiceFileService.saveResult(Long.parseLong(key), analysisResponseDto);
            }
        } else {
            voiceFileService.saveResult(Long.parseLong(key), analysisResponseDto);
        }
    }
}