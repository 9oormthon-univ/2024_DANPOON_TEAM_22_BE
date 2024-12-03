package naeilmolae.domain.pushnotification.strategy.impl;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class OutingNotificationStrategy  implements NotificationStrategy {

    private final MessageSource messageSource;

    // 외출 시간 고정 2시
    private static final LocalDateTime FIXED_OUTING_DATETIME = LocalDateTime.of(2024, 11, 23, 15, 11);


    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return now.getHour() == FIXED_OUTING_DATETIME.getHour() &&
                now.getMinute() == FIXED_OUTING_DATETIME.getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        String message = messageSource.getMessage(
                "notification.outing.message",
                null,
                Locale.getDefault()
        );

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmService.findByAlarmCategory(AlarmCategory.GO_OUT_CLEAR).getId()
        );
    }
}