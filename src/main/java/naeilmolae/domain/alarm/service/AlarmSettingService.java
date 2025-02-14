package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.repository.YouthMemberInfoRepository;
import naeilmolae.domain.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmSettingService {
    private final MemberRepository memberRepository;
    private final YouthMemberInfoRepository youthMemberInfoRepository;


    //    WAKE_UP("기상",CategoryType.DAILY, null),
//    GO_OUT("외출", CategoryType.DAILY, null),
//    MEAL("식사", CategoryType.DAILY, null),
//    SLEEP("취침", CategoryType.DAILY, null),
//    SADNESS("우울과 불안", CategoryType.COMFORT, null),
//    PRAISE("칭찬과 격려", CategoryType.COMFORT, null),
//    CONSOLATION("위로", CategoryType.COMFORT, null),
//    INFO("정보", CategoryType.INFO, null),
    public boolean updateAlarm(Member member, AlarmCategory alarmCategory, boolean alarm) {
        YouthMemberInfo youthMemberInfo = member.getYouthMemberInfo();
        switch (alarmCategory) {
            case WAKE_UP:
                youthMemberInfo.setWakeUpAlarm(alarm);
                break;
            case GO_OUT:
                youthMemberInfo.setOutgoingAlarm(alarm);
                break;
            case MEAL_BREAKFAST:
                youthMemberInfo.setBreakfastAlarm(alarm);
                break;
            case MEAL_LUNCH:
                youthMemberInfo.setLunchAlarm(alarm);
                break;
            case MEAL_DINNER:
                youthMemberInfo.setDinnerAlarm(alarm);
                break;
            case SLEEP:
                youthMemberInfo.setSleepAlarm(alarm);
                break;
            default:
                return false;
        }
        return true;
    }
}
