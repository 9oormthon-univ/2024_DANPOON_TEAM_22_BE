package naeilmolae.domain.pushnotification.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import naeilmolae.domain.pushnotification.strategy.context.NotificationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final MemberAdapterService memberAdapterService;
    private final FirebaseMessagingService firebaseMessagingService; // Firebase 연동
    private final AlarmService alarmService;
    private final NotificationContext notificationContext;

    // 매일 정해진 시간에 알림을 보내는 메서드
    @Transactional(readOnly = true)
    public void sendNotificationsAtScheduledTime() {
        List<Member> youthMembers = memberAdapterService.getAllYouthMember();
        LocalDateTime now = LocalDateTime.now();

        for (Member member : youthMembers) {
            notificationContext.executeStrategies(member, firebaseMessagingService, alarmService, now);
        }
    }

    // 이벤트 발생시 알림
    @Transactional(readOnly = true)
    public void sendNotification(Long memberId, NotificationType notificationType) {
        firebaseMessagingService.sendNotification(memberAdapterService.findById(memberId).getFcmToken(), notificationType);
    }
}
