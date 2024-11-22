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


}
