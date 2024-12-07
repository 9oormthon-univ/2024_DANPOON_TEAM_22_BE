package naeilmolae.domain.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.global.common.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YouthMemberInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime wakeUpTime;

    private LocalDateTime sleepTime;

    private LocalDateTime breakfast;

    private LocalDateTime lunch;

    private LocalDateTime dinner;


    private Double latitude; // 위도

    private Double longitude; // 경도

    private Double gridX; // 격자 좌표의 X (경도)

    private Double gridY; // 격자 좌표의 Y (경도)


    @Builder
    public YouthMemberInfo(
            LocalDateTime wakeUpTime, LocalDateTime sleepTime,
            LocalDateTime breakfast, LocalDateTime lunch, LocalDateTime dinner,
            Double latitude, Double longitude,
            Double gridX, Double gridY) {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public void updateYouthMemberInfoDto(YouthMemberInfoDto youthMemberInfoDto) {
        this.wakeUpTime = youthMemberInfoDto.getWakeUpTime();
        this.sleepTime = youthMemberInfoDto.getSleepTime();
        this.breakfast = youthMemberInfoDto.getBreakfast();
        this.lunch = youthMemberInfoDto.getLunch();
        this.dinner = youthMemberInfoDto.getDinner();
        this.latitude = youthMemberInfoDto.getLatitude();
        this.longitude = youthMemberInfoDto.getLongitude();
    }

    public void setGrid(Double gridX, Double gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }
}
