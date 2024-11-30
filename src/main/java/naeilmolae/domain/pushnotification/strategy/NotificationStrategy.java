package naeilmolae.domain.pushnotification.strategy;

import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;

import java.time.LocalDateTime;

public interface NotificationStrategy {
    boolean shouldSend(YouthMemberInfo info, LocalDateTime now);

    void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService);

}
