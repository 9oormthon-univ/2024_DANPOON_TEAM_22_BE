package naeilmolae.domain.alarm.repository;

import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("SELECT a FROM Alarm a " +
//            "JOIN FETCH aa.parent " +
            "WHERE a.id = :id")
    Optional<Alarm> findById(Long id);

    @Query("SELECT a FROM Alarm a " +
            "WHERE a.alarmCategory = :alarmCategory")
    Optional<Alarm> findByAlarmCategoryId(AlarmCategory alarmCategory);

//    // TODO: 분석에서 실패한 파일도 가져오게됨
//    // TODO: VoiceFile에 대한 의존성을 가지고 있음.
//    @Query("SELECT va " +
//            "FROM VoiceFile vf " +
//            "JOIN vf.alarm va " +
//            "JOIN FETCH va.alarmCategory vaa " +
//            "WHERE vf.member.id = :memberId AND vf.createdAt >= :startDay AND vf.createdAt < :endDay")
//    List<Alarm> findByMemberIdAndBetween(@Param("memberId") Long memberId,
//                                         @Param("startDay") LocalDateTime startDay,
//                                         @Param("endDay") LocalDateTime endDay);

    @Query("SELECT a FROM Alarm a WHERE a.alarmCategory IN :categories")
    List<Alarm> findByCategories(@Param("categories") List<AlarmCategory> categories);

    @Query("SELECT a FROM Alarm a WHERE a.id IN :alarmIds")
    List<Alarm> findByIdIn(@Param("alarmIds") Set<Long> alarmIds);


}
