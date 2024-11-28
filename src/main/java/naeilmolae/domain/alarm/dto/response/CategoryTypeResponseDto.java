package naeilmolae.domain.alarm.dto.response;

import lombok.Getter;
import naeilmolae.domain.alarm.domain.CategoryType;

@Getter
public class CategoryTypeResponseDto {
    private CategoryType categoryType;
    private String koreanName;

    public CategoryTypeResponseDto(CategoryType categoryType) {
        this.categoryType = categoryType;
        this.koreanName = categoryType.getDescription();
    }
}
