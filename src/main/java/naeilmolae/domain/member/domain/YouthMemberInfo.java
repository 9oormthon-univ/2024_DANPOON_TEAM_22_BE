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

    private String address; // 도로명 주소

    private Double latitude; // 위도

    private Double longitude; // 경도

    private String regionCode; // 지역 코드

    @Builder
    public YouthMemberInfo(
            LocalDateTime wakeUpTime, LocalDateTime sleepTime,
            LocalDateTime breakfast, LocalDateTime lunch, LocalDateTime dinner,
            String address, Double latitude, Double longitude, String regionCode) {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionCode = regionCode;
    }

    public void update(YouthMemberInfoDto youthMemberInfoDto) {
        this.wakeUpTime = youthMemberInfoDto.getWakeUpTime();
        this.sleepTime = youthMemberInfoDto.getSleepTime();
        this.breakfast = youthMemberInfoDto.getBreakfast();
        this.lunch = youthMemberInfoDto.getLunch();
        this.dinner = youthMemberInfoDto.getDinner();
        this.address = youthMemberInfoDto.getAddress();
        this.latitude = youthMemberInfoDto.getLatitude();
        this.longitude = youthMemberInfoDto.getLongitude();
        this.regionCode = youthMemberInfoDto.getRegionCode();
    }
}
