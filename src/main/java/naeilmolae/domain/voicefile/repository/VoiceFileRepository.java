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

    @Query("SELECT vf " +
            "FROM VoiceFile vf " +
            "LEFT JOIN vf.alarm vfa " +
            "LEFT JOIN vfa.alarmCategory vfaa " +
            "LEFT JOIN vf.analysisResult as vfas " +
            "WHERE vfa.id = :alarmId " +
            "AND vfas.analysisResultStatus = 'SUCCESS'" +
            "AND NOT EXISTS ( " +
            "    SELECT pf " +
            "    FROM ProvidedFile pf " +
            "    WHERE pf.voiceFile = vf " +
            "    AND pf.consumer.id = :memberId " +
            ") " +
            "AND vf.createdAt >= :oneWeekAgo " + // 최근 1주일 이내 데이터만
            "ORDER BY vf.createdAt ASC")
    List<VoiceFile> findUnprovided(
            @Param("memberId") Long memberId,
            @Param("alarmId") Long alarmId,
            @Param("oneWeekAgo") LocalDateTime oneWeekAgo);

}
