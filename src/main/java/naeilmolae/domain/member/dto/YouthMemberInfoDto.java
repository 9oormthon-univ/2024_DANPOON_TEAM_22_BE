package naeilmolae.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "청년회원 정보 객체")
public class YouthMemberInfoDto {
    private LocalDateTime wakeUpTime;
    private LocalDateTime sleepTime;
    private LocalDateTime breakfast;
    private LocalDateTime lunch;
    private LocalDateTime dinner;
    private Double latitude; // 위도
    private Double longitude; // 경도
}
