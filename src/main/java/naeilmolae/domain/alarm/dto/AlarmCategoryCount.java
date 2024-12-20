package naeilmolae.domain.alarm.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.AlarmCategory;

import java.util.List;

@Getter
public class AlarmCategoryCount {
    private AlarmCategory alarmCategory;
    private String koreanName;
    @Setter
    private String title;
    private boolean isUsed = false;
    private List<AlarmCategory> children;

    public AlarmCategoryCount(AlarmCategory alarmCategory) {
        this.alarmCategory = alarmCategory;
        this.koreanName = alarmCategory.getName();
        this.children = AlarmCategory.getChildrenByParent(alarmCategory);
    }
    public void setUsed() {
        this.isUsed = true;
    }

}
