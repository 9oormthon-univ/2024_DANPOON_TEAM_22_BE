package naeilmolae.domain.alarm.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.AlarmCategory;

import java.util.List;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryCount {
    private AlarmCategory alarmCategory;
    private boolean isUsed = false;
    private List<AlarmCategory> children;

    public AlarmCategoryCount(AlarmCategory alarmCategory) {
        this.alarmCategory = alarmCategory;
        this.children = AlarmCategory.getChildrenByParent(alarmCategory);
    }
    public void setUsed() {
        this.isUsed = true;
    }
}
