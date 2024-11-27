package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryMessageResponseDto;
import naeilmolae.domain.alarm.dto.response.AlarmResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmAdapterService {

    private final AlarmService alarmService;
    private final AlarmCategoryMessageService alarmCategoryMessageService;

    public AlarmCategoryMessageResponseDto findByAlarmCategory(String alarmCategory) {
        AlarmCategory category = AlarmCategory.valueOf(alarmCategory.toUpperCase());
        AlarmCategoryMessage alarmCategoryMessage = alarmCategoryMessageService.findByAlarmCategory(category);
        Alarm alarm = alarmService.findByAlarmCategory(category);
        return new AlarmCategoryMessageResponseDto(alarm, alarmCategoryMessage);
    }

    public AlarmCategoryMessageResponseDto findById(Long alarmId) {
        Alarm alarm = alarmService.findById(alarmId);
        AlarmCategoryMessage alarmCategory = alarmCategoryMessageService.findByAlarmCategory(alarm.getAlarmCategory());
        return new AlarmCategoryMessageResponseDto(alarm, alarmCategory);
    }


    public Map<Long, AlarmResponseDto> findAlarmsByIds(Set<Long> alarmIds) {
        return alarmService.findByIdIn(alarmIds)
                .stream()
                .collect(Collectors.toMap(Alarm::getId, AlarmResponseDto::new));
    }
}
