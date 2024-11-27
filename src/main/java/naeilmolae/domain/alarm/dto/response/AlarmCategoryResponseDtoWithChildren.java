package naeilmolae.domain.alarm.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryResponseDtoWithChildren extends AlarmCategoryResponseDto {
    List<AlarmCategoryResponseDto> children = new ArrayList<>();

    public AlarmCategoryResponseDtoWithChildren(AlarmCategory alarmCategory, AlarmCategoryMessage alarmCategoryMessage) {
        super(alarmCategory, alarmCategoryMessage);
        if (!alarmCategory.isRoot()) {
            throw new IllegalArgumentException("AlarmCategory must be root category"); // TDOO 예외 응답 정제해야함.
        }
        this.children = AlarmCategory.getChildrenByParent(alarmCategory)
                .stream()
                .map(child -> new AlarmCategoryResponseDto(child, alarmCategoryMessage))
                .collect(Collectors.toList());
    }
}
