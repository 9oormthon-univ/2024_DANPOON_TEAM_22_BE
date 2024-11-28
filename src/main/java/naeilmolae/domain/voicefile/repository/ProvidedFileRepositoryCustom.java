package naeilmolae.domain.voicefile.repository;

import naeilmolae.domain.voicefile.domain.ProvidedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvidedFileRepositoryCustom {
    Page<ProvidedFile> findByMemberIdAndAlarmId(Long memberId, List<Long> alarmId, Pageable pageable);

    Page<ProvidedFile> findByMemberId(Long memberId, Pageable pageable);
}
