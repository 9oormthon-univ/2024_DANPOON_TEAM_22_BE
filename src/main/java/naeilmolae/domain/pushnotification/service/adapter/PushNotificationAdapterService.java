package naeilmolae.domain.pushnotification.service.adapter;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import naeilmolae.domain.pushnotification.service.PushNotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushNotificationAdapterService {
    private final PushNotificationService pushNotificationService;

    public void sendNotification(Long memberId, NotificationType notificationType) {
        pushNotificationService.sendNotification(memberId, notificationType);
    }
}