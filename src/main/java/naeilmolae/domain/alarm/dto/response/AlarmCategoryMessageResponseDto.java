package naeilmolae.domain.alarm.dto.response;

import lombok.Getter;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;

@Getter
public class AlarmCategoryMessageResponseDto {
    private Long alarmId;
    private AlarmCategory alarmCategory;
    private String title;

    public AlarmCategoryMessageResponseDto(Alarm alarm, AlarmCategoryMessage alarmCategoryMessage) {
        this.alarmId = alarm.getId();
        this.alarmCategory = alarm.getAlarmCategory();
        this.title = alarmCategoryMessage.getTitle();
    }
}

