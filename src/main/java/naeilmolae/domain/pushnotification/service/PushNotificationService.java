package naeilmolae.domain.pushnotification.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.alarm.service.AlarmViewService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.pushnotification.strategy.context.NotificationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static naeilmolae.domain.alarm.domain.AlarmCategory.*;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final MemberService memberService;
    private final FirebaseMessagingService firebaseMessagingService; // Firebase 연동
    private final AlarmService alarmService;
    private final NotificationContext notificationContext;

    @Transactional(readOnly = true)
    // TODO 수정 필요함
    //todo fcm 토큰 없는 경우 예외 처리 context에서 처리
    public void sendNotificationsAtScheduledTime() {
        List<Member> youthMembers = memberService.getAllYouthMemeber();
        LocalDateTime now = LocalDateTime.now();

        for (Member member : youthMembers) {
            notificationContext.executeStrategies(member, firebaseMessagingService, alarmService, now);
        }
    }
}
