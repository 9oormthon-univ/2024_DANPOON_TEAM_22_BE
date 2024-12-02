package naeilmolae.domain.pushnotification.strategy.impl;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;

import java.time.LocalDateTime;

public class SleepNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getSleepTime() != null &&
                now.getHour() == info.getSleepTime().getHour() &&
                now.getMinute() == info.getSleepTime().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                "오늘 하루 수고 많았어요. 따스한 목소리와 함께 마무리해요.",
                alarmService.findByAlarmCategory(AlarmCategory.MEAL_DINNER).getId()
        );
    }
}