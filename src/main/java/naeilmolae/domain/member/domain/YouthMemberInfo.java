package naeilmolae.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.global.common.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YouthMemberInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime wakeUpTime;
    @Setter
    private boolean wakeUpAlarm = true;

    private LocalDateTime sleepTime;
    @Setter
    private boolean sleepAlarm = true;

    private LocalDateTime breakfast;
    @Setter
    private boolean breakfastAlarm = true;

    private LocalDateTime lunch;
    @Setter
    private boolean lunchAlarm = true;

    private LocalDateTime dinner;
    @Setter
    private boolean dinnerAlarm = true;

    @Setter
    private boolean outgoingAlarm = true;


    private Double latitude; // 위도

    private Double longitude; // 경도

    @Setter
    private Long gridId;


    @Builder
    public YouthMemberInfo(
            LocalDateTime wakeUpTime, LocalDateTime sleepTime,
            LocalDateTime breakfast, LocalDateTime lunch, LocalDateTime dinner,
            Double latitude, Double longitude, Long gridId) {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gridId = gridId;
    }

    // 빈값이거나 없는 값이 아닌 경우에만 업데이트
    public void updateYouthMemberInfoDto(YouthMemberInfoDto youthMemberInfoDto) {
        Optional.ofNullable(youthMemberInfoDto.getWakeUpTime()).ifPresent(value -> this.wakeUpTime = value);
        Optional.ofNullable(youthMemberInfoDto.getSleepTime()).ifPresent(value -> this.sleepTime = value);
        Optional.ofNullable(youthMemberInfoDto.getBreakfast()).ifPresent(value -> this.breakfast = value);
        Optional.ofNullable(youthMemberInfoDto.getLunch()).ifPresent(value -> this.lunch = value);
        Optional.ofNullable(youthMemberInfoDto.getDinner()).ifPresent(value -> this.dinner = value);
        Optional.ofNullable(youthMemberInfoDto.getLatitude()).ifPresent(value -> this.latitude = value);
        Optional.ofNullable(youthMemberInfoDto.getLongitude()).ifPresent(value -> this.longitude = value);
    }


//    public void setGrid(Double gridX, Double gridY) {
//        this.gridX = gridX;
//        this.gridY = gridY;
//    }
}
