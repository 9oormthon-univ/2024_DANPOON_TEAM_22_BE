package naeilmolae.domain.alarm.repository;

import naeilmolae.domain.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("SELECT a FROM Alarm a " +
            "JOIN FETCH a.alarmCategory aa " +
            "JOIN FETCH aa.parent " +
            "WHERE a.id = :id")
    Optional<Alarm> findById(Long id);

    @Query("SELECT a FROM Alarm a " +
            "JOIN FETCH a.alarmCategory aa " +
            "WHERE aa.id = :alarmCategoryId")
    Optional<Alarm> findByAlarmCategoryId(Long alarmCategoryId);

    // TODO: 분석에서 실패한 파일도 가져오게됨
    @Query("SELECT va " +
            "FROM VoiceFile vf " +
            "JOIN vf.alarm va " +
            "JOIN FETCH va.alarmCategory vaa " +
            "JOIN FETCH vaa.parent " +
            "WHERE vf.member.id = :memberId AND vf.createdAt >= :startDay AND vf.createdAt < :endDay")
    List<Alarm> findByMemberIdAndBetween(@Param("memberId") Long memberId,
                                         @Param("startDay") LocalDateTime startDay,
                                         @Param("endDay") LocalDateTime endDay);

    @Query("SELECT a FROM Alarm a " +
            "JOIN FETCH a.alarmCategory ac " +
            "JOIN FETCH ac.parent acp " +
            "WHERE acp.id = :parentId")
    List<Alarm> findAlarmsByParentCategoryId(@Param("parentId") Long parentId);
}
