package naeilmolae.domain.pushnotification.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final MemberService memberService;
    private final FirebaseMessagingService firebaseMessagingService; // Firebase 연동

    public void sendNotificationsAtScheduledTime() {
        // 모든 청년(YOUTH) 유저 가져오기
        List<Member> youthMembers = memberService.getAllYouthMemeber();

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        Long defaultAlarmId = 16L; // 하드코딩된 파일 ID

        for (Member member : youthMembers) {
            YouthMemberInfo info = member.getYouthMemberInfo();

            if (info != null) {
                // wakeUpTime 알림
                if (isTimeToSend(info.getWakeUpTime(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "좋은 아침이에요!", //todo: title하고 body 차이
                            "응원의 목소리와 하루를 시작해볼까요?",
                            defaultAlarmId
                    );
                }

                // breakfast 알림
                if (isTimeToSend(info.getBreakfast(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "밥은 잘 챙겨 먹었나요?",
                            "안 먹었다면, 잠깐 들어봐요.",
                            defaultAlarmId
                    );
                }

                // lunch 알림
                if (isTimeToSend(info.getLunch(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "점심시간이에요!",
                            "맛있게 드세요!",
                            defaultAlarmId
                    );
                }

                // dinner 알림
                if (isTimeToSend(info.getDinner(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "저녁시간이에요!",
                            "저녁 맛있게 드세요!",
                            defaultAlarmId
                    );
                }

                // sleepTime 알림
                if (isTimeToSend(info.getSleepTime(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "오늘 하루도 고생했어요!",
                            "따뜻한 목소리와 함께 하루를 마무리해요.",
                            defaultAlarmId
                    );
                }
            }
        }
    }

    private boolean isTimeToSend(LocalDateTime scheduledTime, LocalDateTime now) {
        // 현재 시간과 스케줄 시간 비교 (1분 간격 내로 처리)
        return scheduledTime != null &&
                now.getHour() == scheduledTime.getHour() &&
                now.getMinute() == scheduledTime.getMinute();
    }
}
