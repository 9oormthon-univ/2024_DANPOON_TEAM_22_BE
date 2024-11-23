package naeilmolae.domain.pushnotification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    public void sendNotification(String fcmToken, String title, Long fileId) {
        // FirebaseMessage 생성
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .build())
                .putData("alarmId", String.valueOf(fileId)) // 파일 ID를 데이터로 추가
                .build();

        try {
            // FCM에 메시지 전송
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}
