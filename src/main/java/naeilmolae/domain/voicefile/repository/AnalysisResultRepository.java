package naeilmolae.domain.voicefile.repository;

import naeilmolae.domain.voicefile.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    Optional<AnalysisResult> findByVoiceFileId(Long voiceFileId);
}
