package naeilmolae.domain.alarm.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import naeilmolae.domain.alarm.domain.AlarmCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class AlarmCategoryResponseDtoWithChildren extends AlarmCategoryResponseDto {
    List<AlarmCategoryResponseDto> children = new ArrayList<>();

    public AlarmCategoryResponseDtoWithChildren(AlarmCategory entity) {
        super(entity);
        if (!entity.isRoot()) {
            throw new IllegalArgumentException("AlarmCategory must be root category"); // TDOO 예외 응답 정제해야함.
        }
        setChildren(entity.getChildren()
                .stream()
                .map(AlarmCategoryResponseDto::new)
                .collect(Collectors.toList()));
        ;
    }
}
