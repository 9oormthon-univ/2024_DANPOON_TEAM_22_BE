package naeilmolae.domain.pushnotification.strategy.impl;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.pushnotification.service.FirebaseMessagingService;
import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

import static naeilmolae.domain.alarm.domain.AlarmCategory.WAKE_UP_WEEKDAY;

@Component
@RequiredArgsConstructor
public class WakeUpNotificationStrategy implements NotificationStrategy {

    private final MessageSource messageSource;

    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getWakeUpTime() != null &&
                now.getHour() == info.getWakeUpTime().getHour() &&
                now.getMinute() == info.getWakeUpTime().getMinute();
    }

    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        String message = messageSource.getMessage(
                "notification.wakeup.message",
                null,
                Locale.getDefault()
        );

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmService.findByAlarmCategory(WAKE_UP_WEEKDAY).getId()
        );
    }
}
