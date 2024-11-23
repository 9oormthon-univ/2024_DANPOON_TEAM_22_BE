package naeilmolae.domain.pushnotification.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.service.AlarmViewService;
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
    private final AlarmViewService alarmViewService;

    // 외출 시간 고정 2시
    private static final LocalDateTime FIXED_OUTING_DATETIME = LocalDateTime.of(2024, 11, 23, 9, 0);


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
                            "좋은 아침이에요! 응원의 목소리와 하루를 시작해볼까요?",
                            alarmViewService.getAlarmCategoryIdByUnqiueId(1001L)
                    );
                }

                // breakfast 알림
                if (isTimeToSend(info.getBreakfast(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "밥은 잘 챙겨 먹었나요? 안 먹었다면, 잠깐 들어봐요.",
                            alarmViewService.getAlarmCategoryIdByUnqiueId(3001L)
                    );
                }

                // lunch 알림
                if (isTimeToSend(info.getLunch(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "밥은 잘 챙겨 먹었나요? 안 먹었다면, 잠깐 들어봐요.",
                            alarmViewService.getAlarmCategoryIdByUnqiueId(3002L)
                    );
                }

                // dinner 알림
                if (isTimeToSend(info.getDinner(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "밥은 잘 챙겨 먹었나요? 안 먹었다면, 잠깐 들어봐요.",
                            alarmViewService.getAlarmCategoryIdByUnqiueId(3003L)
                    );
                }

                // sleepTime 알림
                if (isTimeToSend(info.getSleepTime(), now)) {
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "오늘 하루 수고 많았어요. 따스한 목소리와 함께 마무리해요.",
                            alarmViewService.getAlarmCategoryIdByUnqiueId(4001L)
                    );
                }

                // 외출 시간 알림
                if(isTimeToSend(FIXED_OUTING_DATETIME, now)){
                    firebaseMessagingService.sendNotification(
                            member.getFcmToken(),
                            "외출할 일이 있나요? 나가지 전에, 잠깐 들어봐요.",
                            alarmViewService.getAlarmCategoryIdByUnqiueId(2003L)
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
