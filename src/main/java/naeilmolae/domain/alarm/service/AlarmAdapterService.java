package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import naeilmolae.domain.alarm.domain.AlarmExample;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryMessageResponseDto;
import naeilmolae.domain.alarm.dto.response.AlarmResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmAdapterService {

    private final AlarmService alarmService;
    private final AlarmCategoryMessageService alarmCategoryMessageService;
    private final AlarmExampleService alarmExampleService;

    public List<AlarmResponseDto> findAlarmIdsByAlarmCategory(String parentAlarmCategory) {
        AlarmCategory category = AlarmCategory.valueOf(parentAlarmCategory.toUpperCase());
        List<AlarmCategory> childrenByParent = AlarmCategory.getChildrenByParent(category);
        return alarmService.findByChildrenCategories(childrenByParent)
                .stream()
                .map(AlarmResponseDto::new)
                .toList();
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

    public AlarmExample findAllByAlarmId(Long alarmId) {
        return alarmExampleService.findAllByAlarmId(alarmId);
    }
}
