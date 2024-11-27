package naeilmolae.domain.alarm.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryCount {
    private String title;
    private Long count = 0L;
    private boolean isUsed = false;

    public AlarmCategoryCount(String title, Long count) {
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
