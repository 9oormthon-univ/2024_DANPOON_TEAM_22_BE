package naeilmolae.domain.voicefile.repository;


import jakarta.persistence.Column;
import naeilmolae.domain.voicefile.domain.ProvidedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProvidedFileRepository extends JpaRepository<ProvidedFile, Long> {

    // consumerId, VoiceFileId로 파일 조회
    Optional<ProvidedFile> findByConsumerIdAndVoiceFileId(Long consumerId, Long voiceFileId);

    @Query("SELECT pf FROM ProvidedFile pf JOIN FETCH pf.voiceFile WHERE pf.id = :id")
    Optional<ProvidedFile> findById(Long id);

    @Query("""
            SELECT p 
            FROM ProvidedFile p 
            WHERE p.consumerId = :consumerId 
            AND p.id = :providedFileId""")
    Optional<ProvidedFile> findByConsumerId(Long consumerId, Long providedFileId);

    @Query("SELECT pf.thanksMessages " +
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

    @Query(value = """
        select pf 
        from ProvidedFile pf 
        JOIN FETCH pf.voiceFile 
        where pf.voiceFile.memberId = :memberId 
        and pf.voiceFile.alarmId in :alarmIds
        and not exists (
            select 1 
            from ProvidedFileReport pfr 
            where pfr.providedFile = pf
        )
        """,
            countQuery = """
        select count(pf) 
        from ProvidedFile pf 
        where pf.voiceFile.memberId = :memberId
        and pf.voiceFile.alarmId in :alarmIds
        and not exists (
            select 1 
            from ProvidedFileReport pfr 
            where pfr.providedFile = pf
        )
        """)
    Page<ProvidedFile> findByMemberIdAndAlarmId(Long memberId, List<Long> alarmIds, Pageable pageable);

    @Query(value = """
        SELECT pf 
        FROM ProvidedFile pf
        LEFT JOIN pf.voiceFile vf
        LEFT JOIN ProvidedFileReport pr ON pr.providedFile = pf
        WHERE vf.memberId = :memberId
        AND pr.id IS NULL
        """,
            countQuery = """
        SELECT COUNT(pf) 
        FROM ProvidedFile pf
        LEFT JOIN pf.voiceFile vf
        LEFT JOIN ProvidedFileReport pr ON pr.providedFile = pf
        WHERE vf.memberId = :memberId
        AND pr.id IS NULL
        """)
    @EntityGraph(attributePaths = {"voiceFile"})  // voiceFile을 함께 가져옴
    Page<ProvidedFile> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);



}
