package naeilmolae.domain.member.mapper;

import java.time.LocalDateTime;

public class DefaultTimeSettings {

    public static final LocalDateTime DEFAULT_WAKE_UP_TIME = LocalDateTime.of(1970, 1, 1, 8, 0);
    public static final LocalDateTime DEFAULT_SLEEP_TIME = LocalDateTime.of(1970, 1, 1, 22, 0);
    public static final LocalDateTime DEFAULT_BREAKFAST_TIME = LocalDateTime.of(1970, 1, 1, 8, 30);
    public static final LocalDateTime DEFAULT_LUNCH_TIME = LocalDateTime.of(1970, 1, 1, 12, 0);
    public static final LocalDateTime DEFAULT_DINNER_TIME = LocalDateTime.of(1970, 1, 1, 19, 0);
}
