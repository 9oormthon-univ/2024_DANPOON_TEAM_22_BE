package naeilmolae.domain.alarm.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryCount {
    private Long categoryId;
    private String title;
    private Long count = 0L;
    private boolean isUsed = false;

    public AlarmCategoryCount(Long categoryId, String title, Long count) {
        setCategoryId(categoryId);
        setTitle(title);
        setCount(count);
    }

    public void addCount(Long count) {
        this.count += count;
    }

    public void setUsed() {
        this.isUsed = true;
    }
}
