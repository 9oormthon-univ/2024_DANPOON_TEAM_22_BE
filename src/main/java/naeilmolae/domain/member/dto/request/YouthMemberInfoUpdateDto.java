package naeilmolae.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouthMemberInfoUpdateDto {
    private LocalTime wakeUpTime;
    private LocalTime sleepTime;
    private LocalTime breakfast;
    private LocalTime lunch;
    private LocalTime dinner;
    private LocalTime outgoingTime;
}
