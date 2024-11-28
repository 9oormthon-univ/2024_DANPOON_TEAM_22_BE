package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.repository.AlarmRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {
    private final AlarmRepository alarmRepository;

    // 알람 조회
    public Alarm findById(Long id) {
        return alarmRepository.findById(id)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    public List<Alarm> findByIdIn(Set<Long> alarmIds) {
        return alarmRepository.findByIdIn(alarmIds);
    }


    // 알람 카테고리 ID로 알람 조회
    public Alarm findByAlarmCategory(AlarmCategory alarmCategory) {
        return alarmRepository.findByAlarmCategoryId(alarmCategory)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._BAD_REQUEST));
    }

    public List<Alarm> findByParentCategories(List<AlarmCategory> alarmCategories) {
        List<AlarmCategory> query = new ArrayList<>();
        for (AlarmCategory alarmCategory : alarmCategories) {
            List<AlarmCategory> childrenByParent = AlarmCategory.getChildrenByParent(alarmCategory);
            query.addAll(childrenByParent);
        }
        return alarmRepository.findByCategories(query);
    }

    // TODO 값이 일정하므로 캐싱하게 만들어야 함.
    public List<Alarm> findByChildrenCategories(List<AlarmCategory> alarmCategories) {
        return alarmRepository.findByCategories(alarmCategories);
    }
}
