package naeilmolae.domain.pushnotification.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.pushnotification.strategy.context.NotificationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final MemberService memberService;
    private final FirebaseMessagingService firebaseMessagingService; // Firebase 연동
    private final AlarmService alarmService;
    private final NotificationContext notificationContext;

    @Transactional(readOnly = true)
    public void sendNotificationsAtScheduledTime() {
        List<Member> youthMembers = memberService.getAllYouthMemeber();
        LocalDateTime now = LocalDateTime.now();

        for (Member member : youthMembers) {
            notificationContext.executeStrategies(member, firebaseMessagingService, alarmService, now);
        }
    }
}
