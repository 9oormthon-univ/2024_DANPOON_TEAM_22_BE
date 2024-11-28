package naeilmolae.domain.alarm.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmCategoryResponseDto {
    private String alarmCategory;
    private String alarmCategoryKoreanName;
    private CategoryType categoryType;

    public AlarmCategoryResponseDto from(AlarmCategory alarmCategory) {
        return new AlarmCategoryResponseDto(alarmCategory.name(), alarmCategory.getName(), alarmCategory.getCategoryType());
    }
}
