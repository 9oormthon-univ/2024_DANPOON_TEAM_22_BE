package naeilmolae.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class YouthMemberInfoUpdateDto {
    private LocalTime wakeUpTime;
    private LocalTime sleepTime;
    private LocalTime breakfast;
    private LocalTime lunch;
    private LocalTime dinner;
    private LocalTime outgoingTime;
    private boolean wakeUpAlarm;
    private boolean sleepAlarm ;
    private boolean breakfastAlarm;
    private boolean lunchAlarm;
    private boolean dinnerAlarm;
    private boolean outgoingAlarm;
}
