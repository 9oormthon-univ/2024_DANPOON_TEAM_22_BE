package naeilmolae.domain.voicefile.service;

import com.amazonaws.services.ec2.model.TunnelOption;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.voicefile.domain.ProvidedFile;
import naeilmolae.domain.voicefile.domain.ProvidedFileReport;
import naeilmolae.domain.voicefile.repository.ProvidedFileReportRepository;
import naeilmolae.domain.voicefile.repository.ProvidedFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvidedFileReportService {

    private final ProvidedFileReportRepository providedFileReportRepository;
    private final ProvidedFileRepository providedFileRepository;
    private final MemberAdapterService memberAdapterService;

    @Transactional
    public boolean reportProvidedFile(Long providedFileId, Long reporterId, String reason) {
        // 신고할 ProvidedFile 조회
        ProvidedFile providedFile = providedFileRepository.findById(providedFileId)
                .orElseThrow(() -> new IllegalArgumentException("ProvidedFile not found"));

        // 신고 엔티티 생성
        Member member = memberAdapterService.findById(reporterId);
        ProvidedFileReport report = new ProvidedFileReport(providedFile, member, reason);

        // ProvidedFile에 신고 추가
        providedFileReportRepository.save(report);

        return true;
    }
}
