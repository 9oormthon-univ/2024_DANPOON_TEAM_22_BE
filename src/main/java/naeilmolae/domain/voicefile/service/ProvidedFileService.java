package naeilmolae.domain.voicefile.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryMessageResponseDto;
import naeilmolae.domain.alarm.service.AlarmAdapterService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.voicefile.domain.ProvidedFile;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.domain.VoiceReactionType;
import naeilmolae.domain.voicefile.dto.response.VoiceFileReactionSummaryResponseDto;
import naeilmolae.domain.voicefile.repository.ProvidedFileRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvidedFileService {
    private final ProvidedFileRepository providedFileRepository;
    private final MemberService memberService;
    private final VoiceFileService voiceFileService;
    // 외부 서비스
    private final AlarmAdapterService alarmAdapterService;

    @Transactional
    public ProvidedFile save(Long memberId, Long voiceFileId) {
        // 이미 제공한 파일인지 확인
        providedFileRepository.findByConsumerIdAndVoiceFileId(memberId, voiceFileId)
                .ifPresent(file -> {
                    throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
                });

        VoiceFile voiceFile = voiceFileService.findById(voiceFileId);
        Member consumer = memberService.findById(memberId);


        ProvidedFile providedFile = new ProvidedFile(voiceFile, consumer.getId());

        return providedFileRepository.save(providedFile);
    }

    // 봉사자용 편지 조회
    public Page<ProvidedFile> getProvidedFiles(Long memberId, String parentCategory, Pageable pageable) {
        if (parentCategory == null) {
            return providedFileRepository.findByMemberId(memberId, pageable);
        } else {
            AlarmCategoryMessageResponseDto dto = alarmAdapterService.findByAlarmCategory(parentCategory);
            return providedFileRepository.findByMemberIdAndAlarmId(memberId, dto.getAlarmId(), pageable);
        }
    }

    // 감사 편지 보내기
    @Transactional
    public boolean likeProvidedFile(Long consumerId, Long providedFileId, String message) {
        ProvidedFile providedFile = providedFileRepository.findByConsumerId(consumerId,providedFileId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._BAD_REQUEST));
        return providedFile.addThanksMessage(message);
    }

    // 음성 파일 북마크하기
    @Transactional
    public boolean bookmarkProvidedFile(Long consumerId, Long providedFileId) {
        ProvidedFile providedFile = providedFileRepository.findByConsumerId(consumerId,providedFileId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._BAD_REQUEST));
        return providedFile.setConsumerSaved();
    }

    // 청년의 반응 보여주기
    public VoiceFileReactionSummaryResponseDto getTotalReaction(Long memberId) {

        Long totalListenCount = getTotalListenCount(memberId);
        Map<VoiceReactionType, Integer> reactionSummary = getReactionSummary(memberId);

        return new VoiceFileReactionSummaryResponseDto(totalListenCount, reactionSummary);
    }

    // 청년의 이모티콘 반응 요약
    private Map<VoiceReactionType, Integer> getReactionSummary(Long memberId) {
        List<String> reactionValues = providedFileRepository.findThankMessagesByMemberId(memberId);

        Map<VoiceReactionType, Integer> reactionSummary = new HashMap<>();
        for (VoiceReactionType reaction : VoiceReactionType.values()) {
            long count = reactionValues.stream()
                    .filter(value -> value.equals(reaction.getValue()))
                    .count();
            reactionSummary.put(reaction, (int) count);
        }

        return reactionSummary;
    }

    //청년 누적 청취 수 반환 (나중에 추가 로직 작성될 수도 있어서 메서드로 따로 뻄)
    private long getTotalListenCount(Long memberId) {
        return providedFileRepository.findTotalListenersByMemberId(memberId);
    }
}
