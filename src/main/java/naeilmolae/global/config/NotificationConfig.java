package naeilmolae.global.config;

import naeilmolae.domain.pushnotification.strategy.NotificationStrategy;
import naeilmolae.domain.pushnotification.strategy.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class NotificationConfig {
    @Bean
    public List<NotificationStrategy> notificationStrategies() {
        return List.of(
                new WakeUpNotificationStrategy(),
                new BreakfastNotificationStrategy(),
                new LunchNotificationStrategy(),
                new DinnerNotificationStrategy(),
                new SleepNotificationStrategy(),
                new OutingNotificationStrategy()
        );
    }
}
