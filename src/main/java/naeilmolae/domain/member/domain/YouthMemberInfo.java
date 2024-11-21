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

    @Builder
    public YouthMemberInfo(
            LocalDateTime wakeUpTime, LocalDateTime sleepTime, LocalDateTime breakfast, LocalDateTime lunch, LocalDateTime dinner) {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public void update(YouthMemberInfoDto youthMemberInfoDto) {
        this.wakeUpTime = youthMemberInfoDto.getWakeUpTime();
        this.sleepTime = youthMemberInfoDto.getSleepTime();
        this.breakfast = youthMemberInfoDto.getBreakfast();
        this.lunch = youthMemberInfoDto.getLunch();
        this.dinner = youthMemberInfoDto.getDinner();
    }
}
