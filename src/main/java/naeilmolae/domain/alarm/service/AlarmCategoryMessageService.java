package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import naeilmolae.domain.alarm.repository.AlarmCategoryMessageRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리에 대한 메시지가 존재하지 않습니다.")); // TODO 에러 메시지 수정
    }

    public List<AlarmCategoryMessage> findByAlarmCategoryIn(List<AlarmCategory> alarmCategories) {
        return alarmCategoryMessageRepository.findByAlarmCategoryIn(alarmCategories);
    }

}
