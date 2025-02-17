package naeilmolae.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.service.AlarmSettingService;
import naeilmolae.domain.member.controller.MemberController;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.pushnotification.domain.NotificationType;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@Tag(name = "알람 설정 API", description = "알람 설정을 변경하는 API")
@RequestMapping("/api/v1/alarm-setting")
public class AlarmSettingController {

    private final AlarmSettingService alarmSettingService;

    private final MemberService memberService;


    @Operation(summary = "청년 알림 설정 수정 API", description = "청년 알림 설정 수정 API")
    @PostMapping("/toggle/{alarmCategory}/{bool}")
    public void toggleAlarmSetting(@CurrentMember Member member, @PathVariable AlarmCategory alarmCategory, @PathVariable boolean bool) {
        alarmSettingService.updateAlarm(member, alarmCategory, bool);
    }

    @Operation(summary = "봉사자 알림 설정 수정 API", description = "봉사자 알림 설정 수정 API")
    @PostMapping("/toggle/helper/{notificationType}/{bool}")
    public void toggleAlarmSetting(@CurrentMember Member member, @PathVariable NotificationType notificationType, @PathVariable boolean bool) {
        alarmSettingService.updateHelperAlarm(member, notificationType, bool);
    }

}
