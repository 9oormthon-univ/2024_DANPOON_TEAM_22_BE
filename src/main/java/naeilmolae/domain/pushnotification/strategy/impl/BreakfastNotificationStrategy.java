package naeilmolae.domain.pushnotification.strategy.impl;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;

import java.time.LocalDateTime;

public class BreakfastNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getBreakfast() != null &&
                now.getHour() == info.getBreakfast().getHour() &&
                now.getMinute() == info.getBreakfast().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                "좋은 아침이에요! 응원의 목소리와 하루를 시작해볼까요?",
                alarmService.findByAlarmCategory(AlarmCategory.MEAL_BREAKFAST).getId()
        );
    }
}
