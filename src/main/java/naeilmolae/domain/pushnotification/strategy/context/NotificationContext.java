package naeilmolae.domain.pushnotification.strategy.context;

import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationContext {
    private final List<NotificationStrategy> strategies;

    public NotificationContext(List<NotificationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void executeStrategies(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService, LocalDateTime now) {
        YouthMemberInfo info = member.getYouthMemberInfo();

        if (info == null || member.getFcmToken().isEmpty()) {
            return; // todo 유효하지 않은 사용자(fcm 토큰 없는 사용자) 예외 처리
        }

        for (NotificationStrategy strategy : strategies) {
            if (strategy.shouldSend(info, now)) {
                strategy.send(member, firebaseMessagingService, alarmService);
            }
        }
    }
}
