package naeilmolae.domain.pushnotification.strategy.impl;

import com.google.api.client.util.ArrayMap;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OutingNotificationStrategy  implements NotificationStrategy {

    private final MessageSource messageSource;

    private Map<Long, AlarmCategory> alarmCategoryMap = new ArrayMap<>();

    // 외출 시간 고정 2시
    private static final LocalDateTime FIXED_OUTING_DATETIME = LocalDateTime.of(2024, 11, 23, 15, 11);

    public boolean updateAlarmCategoryMap(Map<Long, AlarmCategory> result) {
        alarmCategoryMap = result;

        return true;
    }

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

//        alarmCategoryMap.getOrDefault(member.getId(), AlarmCategory.GO_OUT_CLEAR);

        // TEST
        AlarmCategory category = alarmCategoryMap.get(member.getId());
        if(category == null) {
            System.out.println("category is null");
            category = AlarmCategory.GO_OUT_CLEAR;
        }

        firebaseMessagingService.sendNotification(
                member.getFcmToken(),
                message,
                alarmService.findByAlarmCategory(AlarmCategory.GO_OUT_CLEAR).getId()
        );
    }
}