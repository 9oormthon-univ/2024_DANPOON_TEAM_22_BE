package naeilmolae.domain.pushnotification.strategy.impl;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;

import java.time.LocalDateTime;

public class DinnerNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getDinner() != null &&
                now.getHour() == info.getDinner().getHour() &&
                now.getMinute() == info.getDinner().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                "밥은 잘 챙겨 먹었나요? 안 먹었다면, 잠깐 들어봐요.",
                alarmService.findByAlarmCategory(AlarmCategory.MEAL_DINNER).getId()
        );
    }
}