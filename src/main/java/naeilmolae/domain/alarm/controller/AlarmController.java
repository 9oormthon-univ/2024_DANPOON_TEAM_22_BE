package naeilmolae.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.CategoryType;
import naeilmolae.domain.alarm.dto.AlarmCategoryCount;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryResponseDto;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryResponseDtoWithChildren;
import naeilmolae.domain.alarm.dto.response.AlarmResponseDto;
import naeilmolae.domain.alarm.service.AlarmCategoryService;
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
    private final AlarmCategoryService alarmCategoryService;
    private final AlarmService alarmService;

    // valid
    @Operation(summary = "[VALID] [청년] 위로 청취 1단계: 위로 목록 조회", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/comfort")
    public BaseResponse<List<AlarmCategoryResponseDtoWithChildren>> getComfortList(@CurrentMember Member member) {
        List<AlarmCategoryResponseDtoWithChildren> collect = alarmCategoryService.findComfortAlarmCategoryList()
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

    @Operation(summary = "[VALID] [봉사자] 녹음 1단계: 녹음 선택 리스트 조회", description = "사용자의 녹음할 리스트를 조회합니다. 정렬은 1. 사용자가 작성하지 않음 2. 데이터 수가 적음 순으로 보여지게 됩니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공"),
    })
    @GetMapping("/list/{categoryType}")
    public BaseResponse<List<AlarmCategoryCount>> getAlarmList(@CurrentMember Member member,
                                                               @PathVariable CategoryType categoryType) {
        List<AlarmCategoryCount> collect = alarmViewService.findUserCategoryCount(member.getId(), categoryType);
        return BaseResponse.onSuccess(collect);
    }

    @Operation(summary = "[VALID] [봉사자] 녹음 2단계: 말 작성 시 상단 멘트 조회", description = "위로 녹음9 에서 상단에 보여줄 멘트를 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공"),
    })
    @GetMapping("/alarm-category/{alarmCategoryId}/optimized")
    public BaseResponse<AlarmResponseDto> getAlarmCategory(@CurrentMember Member member,
                                                           @PathVariable Long alarmCategoryId) {
        Alarm recommendedAlarm = alarmViewService.findRecommendedAlarm(member.getId(), alarmCategoryId);
        String title = recommendedAlarm.getAlarmCategory().getTitle();
        Long id = recommendedAlarm.getId();
        AlarmResponseDto alarmResponseDto = new AlarmResponseDto(id, title);
        return BaseResponse.onSuccess(alarmResponseDto);
    }

    @Operation(summary = "[VALID] [봉사자] 동기부여 1단계: 북두칠성 조회 API", description = "사용자의 북두칠성을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AlarmCategoryResponseDto.class)))
    })
    @GetMapping("/this-week")
    public BaseResponse<List<AlarmCategoryResponseDto>> getDistinctAlarmTypesThisWeek(@CurrentMember Member member) {
        List<AlarmCategoryResponseDto> collect = alarmViewService.findDistinctCreatedCategories(member.getId())
                .stream()
                .map(AlarmCategoryResponseDto::new)
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(collect);
    }

    @Operation(summary = "[VALID] [봉사자] 동기부여 4단계: 카테고리 목록 조회", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/")
    public BaseResponse<List<AlarmCategoryResponseDto>> getAlarmCategoryList(@CurrentMember Member member) {
        List<AlarmCategoryResponseDto> list = alarmService.findParentCateories()
                .stream()
                .map(AlarmCategoryResponseDto::new).toList();

        return BaseResponse.onSuccess(list);
    }
}
