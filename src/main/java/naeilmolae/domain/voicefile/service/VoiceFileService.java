package naeilmolae.domain.voicefile.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.voicefile.domain.AnalysisResultStatus;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.response.AnalysisResponseDto;
import naeilmolae.domain.voicefile.evnets.VoiceFileAnalysisEvent;
import naeilmolae.domain.voicefile.repository.VoiceFileRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AnalysisErrorStatus;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import naeilmolae.global.common.exception.code.status.VoiceFileErrorStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoiceFileService {
    private final MemberService memberService;
    private final AlarmService alarmService;
    private final VoiceFileRepository voiceFileRepository;
    private final ApplicationEventPublisher publisher;

    // 음성 파일 저장
    @Transactional
    public VoiceFile saveContent(Long memberId, Long alarmId, String content) {
        // 스크립트 검증
        verifyContent(content);

        Member member = memberService.findById(memberId);
        Alarm alarm = alarmService.findById(alarmId);

        VoiceFile voiceFile = new VoiceFile(member, alarm, content);

        return voiceFileRepository.save(voiceFile);
    }

    // 음성 파일 스크립트 검증 (gpt 사용)
    private void verifyContent(String content) {
//        if(chatGptService.checkForOffensiveLanguage(content)){
//            throw new RestApiException(AnalysisErrorStatus._INCLUDE_INAPPROPRIATE_CONTENT);
//        }
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
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        if (voiceFile.getFileUrl() == null) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
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
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

}
