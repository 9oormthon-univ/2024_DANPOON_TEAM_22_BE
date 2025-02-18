package naeilmolae.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@Schema(description = "청년회원 정보 객체")
public class YouthMemberInfoDto {
    private Double latitude; // 위도
    private Double longitude; // 경도
    private LocalTime wakeUpTime;
    private LocalTime sleepTime;
    private LocalTime breakfast;
    private LocalTime lunch;
    private LocalTime dinner;
}
