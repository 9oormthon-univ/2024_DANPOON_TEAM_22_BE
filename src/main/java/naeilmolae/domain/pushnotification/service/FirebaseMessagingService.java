package naeilmolae.domain.pushnotification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseMessagingService.class);

    public void sendNotification(String fcmToken, String title, Long alarmId) {
        // FirebaseMessage 생성
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .build())
                .putData("alarmId", String.valueOf(alarmId)) // 파일 ID를 데이터로 추가
                .build();

        try {
            // FCM에 메시지 전송
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Successfully sent message: {}", response);
            logger.info("Notification details - FCM Token: {}, Title: {}, Alarm ID: {}", fcmToken, title, alarmId);
        } catch (Exception e) {
            logger.error("Error sending message: {}", e.getMessage());
            logger.error("Failed to send notification to FCM Token: {}, Title: {}, Alarm ID: {}", fcmToken, title, alarmId, e);
        }
    }
}
