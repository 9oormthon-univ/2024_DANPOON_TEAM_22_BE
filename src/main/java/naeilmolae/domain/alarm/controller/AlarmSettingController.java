package naeilmolae.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.service.AlarmSettingService;
import naeilmolae.domain.member.controller.MemberController;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm-setting")
public class AlarmSettingController {

    private final AlarmSettingService alarmSettingService;

    private final MemberService memberService;

    @PostMapping("/toggle/{alarmCategory}/{bool}")
    public void toggleAlarmSetting(@CurrentMember Member member, @PathVariable AlarmCategory alarmCategory, @PathVariable boolean bool) {
        alarmSettingService.updateAlarm(member, alarmCategory, bool);
    }

}
