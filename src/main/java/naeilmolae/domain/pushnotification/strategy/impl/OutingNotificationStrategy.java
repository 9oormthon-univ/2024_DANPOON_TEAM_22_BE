package naeilmolae.domain.pushnotification.strategy.impl;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;

import java.time.LocalDateTime;

public class OutingNotificationStrategy  implements NotificationStrategy {

    // 외출 시간 고정 2시
    private static final LocalDateTime FIXED_OUTING_DATETIME = LocalDateTime.of(2024, 11, 23, 15, 56);


    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return now.getHour() == FIXED_OUTING_DATETIME.getHour() &&
                now.getMinute() == FIXED_OUTING_DATETIME.getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                "외출할 일이 있나요? 나가지 전에, 잠깐 들어봐요.",
                alarmService.findByAlarmCategory(AlarmCategory.MEAL_DINNER).getId()
        );
    }
}