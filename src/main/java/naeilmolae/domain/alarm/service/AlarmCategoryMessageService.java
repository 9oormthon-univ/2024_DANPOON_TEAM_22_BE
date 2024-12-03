package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import naeilmolae.domain.alarm.repository.AlarmCategoryMessageRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AlarmErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmCategoryMessageService {
    private final AlarmCategoryMessageRepository alarmCategoryMessageRepository;

    public AlarmCategoryMessage findByAlarmCategory(AlarmCategory alarmCategory) {
        return alarmCategoryMessageRepository.findByAlarmCategory(alarmCategory)
                .orElseThrow(() -> new RestApiException(AlarmErrorStatus._NOT_FOUND_BY_CATEGORY));
    }

    public List<AlarmCategoryMessage> findByAlarmCategoryIn(List<AlarmCategory> alarmCategories) {
        return alarmCategoryMessageRepository.findByAlarmCategoryIn(alarmCategories);
    }

}
