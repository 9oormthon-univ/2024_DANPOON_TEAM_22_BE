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

import static naeilmolae.domain.alarm.domain.AlarmCategory.MEAL_BREAKFAST;

@Component
@RequiredArgsConstructor
public class BreakfastNotificationStrategy implements NotificationStrategy {

    private final MessageSource messageSource;

    @Override
    public boolean shouldSend(YouthMemberInfo info, LocalDateTime now) {
        return info.getBreakfast() != null &&
                now.getHour() == info.getBreakfast().getHour() &&
                now.getMinute() == info.getBreakfast().getMinute();
    }

    @Override
    public void send(Member member, FirebaseMessagingService firebaseMessagingService, AlarmService alarmService) {
        String message = messageSource.getMessage(
                "notification.breakfast.message",
                null,
                Locale.getDefault()
        );

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmService.findByAlarmCategory(MEAL_BREAKFAST).getId()
        );
    }
}
