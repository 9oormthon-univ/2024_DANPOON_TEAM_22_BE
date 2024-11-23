package naeilmolae.domain.alarm.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryResponseDto {
    private Long id;
    private String name;
    private String title;
    private CategoryType categoryType;

    public AlarmCategoryResponseDto(AlarmCategory entity) {
        setId(entity.getId());
        setName(entity.getName());
        setTitle(entity.getTitle());
        setCategoryType(entity.getCategoryType());
    }
}
