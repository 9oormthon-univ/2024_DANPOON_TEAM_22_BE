package naeilmolae.domain.voicefile.repository;

import naeilmolae.domain.voicefile.domain.ProvidedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProvidedFileRepositoryCustom {
    Page<ProvidedFile> findByMemberIdAndAlarmId(Long memberId, Long alarmId, Pageable pageable);

    Page<ProvidedFile> findByMemberId(Long memberId, Pageable pageable);
}
