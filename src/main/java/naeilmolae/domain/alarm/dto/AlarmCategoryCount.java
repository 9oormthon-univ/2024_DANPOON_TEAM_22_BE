package naeilmolae.domain.alarm.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.Alarm;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryCount {
    private Long alarmId;
    private boolean isUsed = false;

    public AlarmCategoryCount(Alarm alarm) {
        this.alarmId = alarm.getId();
    }
    public void setUsed() {
        this.isUsed = true;
    }
}
