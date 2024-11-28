package naeilmolae.domain.alarm.dto.response;

import lombok.Getter;
import naeilmolae.domain.alarm.domain.Alarm;

@Getter
public class AlarmResponseDto {
    private Long alarmId;
    private String alarmCategory;

    public AlarmResponseDto(Alarm alarm) {
        this.alarmId = alarm.getId();
        this.alarmCategory = alarm.getAlarmCategory().name();
    }
}
