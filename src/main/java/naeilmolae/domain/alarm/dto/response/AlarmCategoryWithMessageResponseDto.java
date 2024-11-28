package naeilmolae.domain.alarm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import naeilmolae.domain.alarm.domain.CategoryType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryWithMessageResponseDto {
    private AlarmCategory alarmCategory;
    private String alarmCategoryKoreanName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY) // 비어있는 경우 직렬화 제외
    private String title;
    private CategoryType categoryType;
    @JsonInclude(JsonInclude.Include.NON_EMPTY) // 비어있는 경우 직렬화 제외
    private List<AlarmCategoryWithMessageResponseDto> children = new ArrayList<>();

    public AlarmCategoryWithMessageResponseDto(AlarmCategory alarmCategory, AlarmCategoryMessage alarmCategoryMessage) {
        this.alarmCategory = alarmCategory;
        this.alarmCategoryKoreanName = alarmCategory.getName();
        if (alarmCategoryMessage != null) {
            this.title = alarmCategoryMessage.getTitle();
        }
        this.categoryType = alarmCategory.getCategoryType();
    }

    public void addChildren(AlarmCategoryWithMessageResponseDto child) {
        this.children.add(child);
    }
}
