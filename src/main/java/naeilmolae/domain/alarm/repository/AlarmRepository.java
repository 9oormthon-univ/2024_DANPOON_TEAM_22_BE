package naeilmolae.domain.alarm.repository;

import naeilmolae.domain.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("SELECT a FROM Alarm a " +
            "JOIN FETCH a.alarmCategory aa " +
            "LEFT JOIN aa.parent " +
            "WHERE a.id = :id")
    Optional<Alarm> findById(Long id);


}
