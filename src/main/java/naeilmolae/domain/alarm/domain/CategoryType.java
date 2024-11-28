package naeilmolae.domain.alarm.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryType {
    DAILY("일상"),
    COMFORT("위로");

    private final String description;
}
