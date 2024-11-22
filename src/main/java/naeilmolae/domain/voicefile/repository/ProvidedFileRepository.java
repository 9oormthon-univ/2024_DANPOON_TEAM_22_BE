package naeilmolae.domain.voicefile.repository;


import naeilmolae.domain.voicefile.domain.ProvidedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProvidedFileRepository extends JpaRepository<ProvidedFile, Long>{

    // consumerId, VoiceFileId로 파일 조회
    Optional<ProvidedFile> findByConsumerIdAndVoiceFileId(Long consumerId, Long voiceFileId);
}
