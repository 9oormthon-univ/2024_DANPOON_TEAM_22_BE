package naeilmolae.domain.alarm.repository;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmCategoryMessageRepository extends JpaRepository<AlarmCategoryMessage, Long> {
    Optional<AlarmCategoryMessage> findByAlarmCategory(AlarmCategory alarmCategory);

    List<AlarmCategoryMessage> findByAlarmCategoryIn(List<AlarmCategory> alarmCategories);
}
