package naeilmolae.domain.pushnotification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import naeilmolae.domain.pushnotification.domain.NotificationType;
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
        sendNotification(message);
    }

    // 이벤트가 발생하면 알림을 보내는 메서드, 제목과 콘텐트와 fcm 토큰을 받고 이 서비스로 알람을 보내줌
    public void sendNotification(String fcmToken, String title, String content, NotificationType notificationType) {
        // FirebaseMessage 생성
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(content)
                        .build())
                .putData("notificationType", notificationType.name()) // 파일 ID를 데이터로 추가
                .build();
        sendNotification(message);
    }

    // FCM에 메시지 전송
    public void sendNotification(Message message) {
        try {
            // FCM에 메시지 전송
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Successfully sent message: {}", response);
            logger.info("Notification details - message: {}", message.toString());
        } catch (Exception e) {
            logger.error("Error sending message: {}", e.getMessage());
            logger.error("Failed to send notification to message: {}", message.toString());
        }
    }
}
