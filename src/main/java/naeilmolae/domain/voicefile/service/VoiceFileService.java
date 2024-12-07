package naeilmolae.domain.voicefile.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryMessageResponseDto;
import naeilmolae.domain.alarm.service.AlarmAdapterService;
import naeilmolae.domain.chatgpt.dto.ScriptValidationResponseDto;
import naeilmolae.domain.chatgpt.service.ChatGptService;
import naeilmolae.domain.voicefile.domain.AnalysisResult;
import naeilmolae.domain.voicefile.domain.AnalysisResultStatus;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.response.AnalysisResponseDto;
import naeilmolae.domain.voicefile.evnets.VoiceFileAnalysisEvent;
import naeilmolae.domain.voicefile.repository.AnalysisResultRepository;
import naeilmolae.domain.voicefile.repository.VoiceFileRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AnalysisErrorStatus;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import naeilmolae.global.common.exception.code.status.VoiceFileErrorStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoiceFileService {
    private final VoiceFileRepository voiceFileRepository;
    private final ApplicationEventPublisher publisher;
    private final AnalysisResultRepository analysisResultRepository;
    private final ChatGptService chatGptService;
    // 외부 서비스
    private final AlarmAdapterService alarmAdapterService;

    // 음성 파일 저장
    @Transactional
    public VoiceFile saveContent(Long memberId, Long alarmId, String content) {
        AlarmCategoryMessageResponseDto alarmCategoryMessageResponseDto = alarmAdapterService.findById(alarmId);// 알람이 존재하는지 확인
        String title = alarmCategoryMessageResponseDto.getTitle();

        if (!alarmCategoryMessageResponseDto.getAlarmCategory().equals(AlarmCategory.INFO_INFO)) {
            // 스크립트 검증
            verifyContent(title, content);
        }
        VoiceFile voiceFile = new VoiceFile(memberId, alarmId, content);
        return voiceFileRepository.save(voiceFile);
    }

    // 음성 파일 스크립트 검증 (gpt 사용)
    private void verifyContent(String title, String content) {
        ScriptValidationResponseDto checkScriptRelevancePrompt
                = chatGptService.getCheckScriptRelevancePrompt(title, content);
        if (!checkScriptRelevancePrompt.isProper()) {
            throw new RestApiException(AnalysisErrorStatus._DENIED_BY_GPT);
        }
    }

    // 분석 결과 저장
    @Transactional
    public void saveResult(Long voiceFileId, AnalysisResponseDto analysisResponseDto) {
        VoiceFile voiceFile = voiceFileRepository.findById(voiceFileId)
                .orElseThrow(() -> new RestApiException(AnalysisErrorStatus._CANNOT_SAVE_ANALYSIS_RESULT));

        voiceFile.saveResult(AnalysisResultStatus.fromString(analysisResponseDto.analysisResultStatus()),
                analysisResponseDto.sttContent());
    }

    // 음성 파일 URL 저장
    @Transactional
    public boolean saveVoiceFileUrl(Long voiceFileId, String fileUrl) {
        VoiceFile voiceFile = voiceFileRepository.findById(voiceFileId)
                .orElseThrow(() -> new RestApiException(VoiceFileErrorStatus._NO_SUCH_FILE));
        voiceFile.saveFileUrl(fileUrl);
        this.requestAnalysis(voiceFileId);

        return true;
    }

    // 음성 파일에 대해서 분석 요청
    public Long requestAnalysis(Long voiceFileId) {
        // 음성 파일 조회
        VoiceFile voiceFile = voiceFileRepository.findById(voiceFileId)
                .orElseThrow(() -> new RestApiException(VoiceFileErrorStatus._NO_SUCH_FILE));

        if (voiceFile.getFileUrl() == null) {
            throw new RestApiException(VoiceFileErrorStatus._VOICE_FILE_NOT_PROVIDED);
        }

        // 분석 요청 이벤트 발행
        publisher.publishEvent(new VoiceFileAnalysisEvent(voiceFile.getId(),
                voiceFile.getFileUrl(),
                voiceFile.getContent()));

        return voiceFile.getId();
    }

    // 음성 파일 조회
    public VoiceFile findById(Long fileId) {
        return voiceFileRepository.findById(fileId)
                .orElseThrow(() -> new RestApiException(VoiceFileErrorStatus._NO_SUCH_FILE));
    }

    // 분석 결과 조회
    public AnalysisResult getAnalysisResultRepository(Long voiceFileId) {
        AnalysisResult analysisResult = analysisResultRepository.findByVoiceFileId(voiceFileId)
                .orElseThrow(() -> new RestApiException(AnalysisErrorStatus._NOT_YET));
        switch (analysisResult.getAnalysisResultStatus()) {
            case INCLUDE_INAPPROPRIATE_CONTENT:
                throw new RestApiException(AnalysisErrorStatus._INCLUDE_INAPPROPRIATE_CONTENT);
            case NOT_READ_VOICE:
                throw new RestApiException(AnalysisErrorStatus._NOT_READ_VOICE);
            case ERROR:
                throw new RestApiException(AnalysisErrorStatus._ERROR);
            case SUCCESS:
                return analysisResult;
            default:
                throw new RestApiException(AnalysisErrorStatus._ANALYSIS_NOT_YET);
        }
    }

    // 음성 파일이 샤용자의 것이 맞는지
    public VoiceFile verifyUserFile(Long memberId, Long fileId) {
        return voiceFileRepository.findByMemberIdAndId(memberId, fileId)
                .orElseThrow(() -> new RestApiException(VoiceFileErrorStatus._NO_SUCH_FILE));
    }

    // 사용자가 특정 시간 사이에 생성한 알람 ID List 조회
    public List<Long> findAlarmIdsByMemberIdAndBetween(Long memberId, LocalDateTime startDay, LocalDateTime endDay) {
        return voiceFileRepository.findAlarmIdsByMemberIdAndBetween(memberId, startDay, endDay);
    }

    // 알람 Id로 사용자에게 제공되지 않은 데이터 조회
    public VoiceFile getAvailableDataList(Long memberId, Long alarmId) {
        // TODO 청년만 통과할 수 있게
        // TODO 추후에 음성 제공 알고리즘 적용 예정

        List<VoiceFile> unprovided = voiceFileRepository.findUnprovided(memberId, alarmId, LocalDateTime.now().minusWeeks(1));
        VoiceFile voiceFile = unprovided
                .stream()
                .findFirst()
                .orElseThrow(() -> new RestApiException(VoiceFileErrorStatus._LACK_OF_MESSAGE));// 제공할 수 있는 데이터가 없는 경우

        return voiceFile;
    }
}
