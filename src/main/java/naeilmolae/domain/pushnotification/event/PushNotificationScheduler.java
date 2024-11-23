package naeilmolae.domain.pushnotification.event;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.pushnotification.service.PushNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushNotificationScheduler {

    private final PushNotificationService pushNotificationService;

    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void schedulePushNotifications() {
        pushNotificationService.sendNotificationsAtScheduledTime();
    }
}

