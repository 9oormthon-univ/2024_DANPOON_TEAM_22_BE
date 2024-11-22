package naeilmolae.domain.voicefile.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.voicefile.domain.ProvidedFile;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.repository.ProvidedFileRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvidedFileService {
    private final ProvidedFileRepository providedFileRepository;
    private final MemberService memberService;
    private final VoiceFileService voiceFileService;

    @Transactional
    public ProvidedFile save(Long memberId, Long voiceFileId) {
        // 이미 제공한 파일인지 확인
        providedFileRepository.findByConsumerIdAndVoiceFileId(memberId, voiceFileId)
                .ifPresent(file -> {
                    throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
                });

        VoiceFile voiceFile = voiceFileService.findById(voiceFileId);
        Member consumer = memberService.findById(memberId);
        ProvidedFile providedFile = new ProvidedFile(voiceFile, consumer);

        return providedFileRepository.save(providedFile);
    }

}
