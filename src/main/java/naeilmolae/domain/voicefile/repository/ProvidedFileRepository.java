package naeilmolae.domain.voicefile.repository;


import naeilmolae.domain.voicefile.domain.ProvidedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProvidedFileRepository extends JpaRepository<ProvidedFile, Long>, ProvidedFileRepositoryCustom {

    // consumerId, VoiceFileId로 파일 조회
    Optional<ProvidedFile> findByConsumerIdAndVoiceFileId(Long consumerId, Long voiceFileId);

//    @Query("select p from ProvidedFile p " +
//            "join fetch p.consumer " +
//            "join fetch p.voiceFile vf " +
//            "join fetch vf.member " +
//            "where p.voiceFile.member.id = :memberId " +
//            "and vf.alarm.id = :alarmId")
//    Page<ProvidedFile> findByMemberIdAndAlarmId(Long memberId, Long alarmId, Pageable pageable);

//    @Query("select p from ProvidedFile p " +
//            "join fetch p.consumer " +
//            "join fetch p.voiceFile vf " +
//            "join fetch vf.member " +
//            "join fetch vf.alarm vfa " +
//            "join fetch vfa.alarmCategory vfaa " +
//            "join fetch vfaa.parent vfaap " +
//            "where p.voiceFile.member.id = :memberId")
//    Page<ProvidedFile> findByMemberId(Long memberId, Pageable pageable);

    @Query("""
            SELECT p 
            FROM ProvidedFile p 
            WHERE p.consumerId = :consumerId 
            AND p.id = :providedFileId""")
    Optional<ProvidedFile> findByConsumerId(Long consumerId, Long providedFileId);

    @Query("SELECT pf.thanksMessage " +
            "FROM ProvidedFile pf " +
            "JOIN pf.voiceFile vf " +
            "WHERE vf.memberId = :memberId")
        // ProvidedFile의 voiceFile의 member의 id가 memberId인 ProvidedFile의 thanksMessage를 찾는 쿼리
    List<String> findThankMessagesByMemberId(Long memberId);

    @Query("SELECT COUNT(pf) " +
            "FROM ProvidedFile pf " +
            "JOIN pf.voiceFile vf " +
            "WHERE vf.memberId = :memberId")
        // ProvidedFile의 voiceFile의 member의 id가 memberId인 ProvidedFile의 개수를 찾는 쿼리
    Long findTotalListenersByMemberId(@Param("memberId") Long memberId);


}
