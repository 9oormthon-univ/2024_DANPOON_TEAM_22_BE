package naeilmolae.domain.alarm.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import naeilmolae.domain.alarm.domain.CategoryType;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryResponseDto {
    private String name;
    private String title;
    private CategoryType categoryType;

    public AlarmCategoryResponseDto(AlarmCategory alarmCategory, AlarmCategoryMessage alarmCategoryMessage) {
        this.name = alarmCategory.name();
        this.title = alarmCategoryMessage.getTitle();
        this.categoryType = alarmCategory.getCategoryType();
    }
}
