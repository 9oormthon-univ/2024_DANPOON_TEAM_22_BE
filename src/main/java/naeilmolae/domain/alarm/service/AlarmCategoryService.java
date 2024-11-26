package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;
import naeilmolae.domain.alarm.repository.AlarmCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmCategoryService {

    private final AlarmCategoryRepository alarmCategoryRepository;

    public List<AlarmCategory> findComfortAlarmCategoryList() {
        return alarmCategoryRepository.findByCategoryTypeAndParentIsNull(CategoryType.COMFORT);
    }
}
