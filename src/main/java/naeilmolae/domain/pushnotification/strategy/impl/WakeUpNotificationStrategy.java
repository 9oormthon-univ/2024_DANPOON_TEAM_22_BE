package naeilmolae.domain.pushnotification.strategy.impl;

import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;

import java.time.LocalDateTime;

import static naeilmolae.domain.alarm.domain.AlarmCategory.WAKE_UP_WEEKDAY;

public class WakeUpNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getWakeUpTime() != null &&
                now.getHour() == info.getWakeUpTime().getHour() &&
                now.getMinute() == info.getWakeUpTime().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                "밥은 잘 챙겨 먹었나요? 안 먹었다면, 잠깐 들어봐요",
                alarmService.findByAlarmCategory(WAKE_UP_WEEKDAY).getId()
        );
    }
}
