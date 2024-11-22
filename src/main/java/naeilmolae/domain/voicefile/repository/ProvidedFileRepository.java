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

    @Query("select p from ProvidedFile p " +
            "join fetch p.consumer " +
            "join fetch p.voiceFile vf " +
            "join fetch vf.member " +
            "join fetch vf.alarm vfa " +
            "join fetch vfa.alarmCategory vfaa " +
            "where p.voiceFile.member.id = :memberId and p.voiceFile.alarm.alarmCategory.parent.id = :alarmId")
    Page<ProvidedFile> findByMemberIdAndAlarmId(Long memberId, Long alarmId, Pageable pageable);

    @Query("select p from ProvidedFile p " +
            "join fetch p.consumer " +
            "join fetch p.voiceFile vf " +
            "join fetch vf.member " +
            "join fetch vf.alarm vfa " +
            "join fetch vfa.alarmCategory vfaa " +
            "join fetch vfaa.parent vfaap " +
            "where p.voiceFile.member.id = :memberId")
    Page<ProvidedFile> findByMemberId(Long memberId, Pageable pageable);

    @Query("select p from ProvidedFile p join fetch p.consumer where p.consumer.id = :consumerId and p.id = :providedFileId")
    Optional<ProvidedFile> findByConsumerId(Long consumerId, Long providedFileId);
}
