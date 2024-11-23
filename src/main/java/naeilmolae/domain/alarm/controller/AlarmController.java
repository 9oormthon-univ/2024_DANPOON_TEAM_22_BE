package naeilmolae.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryResponseDtoWithChildren;
import naeilmolae.domain.alarm.dto.response.AlarmResponseDto;
import naeilmolae.domain.alarm.service.AlarmService;
import naeilmolae.domain.alarm.service.AlarmViewService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmViewService alarmViewService;
    private final AlarmService alarmService;


    // valid
    @Operation(summary = "[VALID] [청년] 위로 청취 1단계: 위로 목록 조회", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/comfort")
    public BaseResponse<List<AlarmCategoryResponseDtoWithChildren>> getComfortList(@CurrentMember Member member) {
        List<AlarmCategoryResponseDtoWithChildren> collect = alarmViewService.findComfortAlarmCategoryList()
                .stream()
                .map(AlarmCategoryResponseDtoWithChildren::new)
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(collect);
    }

    // valid
    @Operation(summary = "[VALID] [청년] 위로 청취 2단계: 1단계로 AlarmId 조회", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/{alarmCategoryId}")
    public BaseResponse<AlarmResponseDto> getAlarmIdByAlarmCategoryId(@CurrentMember Member member,
                                                                      @PathVariable Long alarmCategoryId) {
        Alarm alarm = alarmService.findByAlarmCategoryId(alarmCategoryId);
        AlarmResponseDto alarmResponseDto = new AlarmResponseDto(alarm.getId(), alarm.getAlarmCategory().getTitle());
        return BaseResponse.onSuccess(alarmResponseDto);
    }

}
