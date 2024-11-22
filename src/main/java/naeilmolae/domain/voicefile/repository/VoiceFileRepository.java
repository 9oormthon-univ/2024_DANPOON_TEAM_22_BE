package naeilmolae.domain.voicefile.repository;

import naeilmolae.domain.voicefile.domain.VoiceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoiceFileRepository extends JpaRepository<VoiceFile, Long> {
    Optional<VoiceFile> findByMemberIdAndId(Long memberId, Long fileId);


}
