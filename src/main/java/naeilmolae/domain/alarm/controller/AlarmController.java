package naeilmolae.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.AlarmCategoryMessage;
import naeilmolae.domain.alarm.domain.CategoryType;
import naeilmolae.domain.alarm.dto.AlarmCategoryCount;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryMessageResponseDto;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryWithMessageResponseDto;
import naeilmolae.domain.alarm.service.AlarmCategoryMessageService;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static naeilmolae.domain.alarm.domain.AlarmCategory.ROOT_CATEGORIES;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmViewService alarmViewService;
    private final AlarmCategoryMessageService alarmCategoryMessageService;

    // valid
    @Operation(summary = "[VALID] [청년] 위로 청취 1단계: 위로 목록 조회 -> '[Common] ChildrenCategoryId로 AlarmId 조회'로 이동", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/comfort")
    public BaseResponse<List<AlarmCategoryWithMessageResponseDto>> getComfortList(@CurrentMember Member member) {
        List<AlarmCategory> list = ROOT_CATEGORIES
                .stream()
                .filter(item -> item.getCategoryType().equals(CategoryType.COMFORT))
                .collect(Collectors.toList());

        List<AlarmCategoryWithMessageResponseDto> collect = alarmCategoryMessageService.findByAlarmCategoryIn(list)
                .stream()
                .map(alarmCategoryMessage -> new AlarmCategoryWithMessageResponseDto(alarmCategoryMessage.getAlarmCategory(), alarmCategoryMessage))
                .collect(Collectors.toList());

        collect.stream()
                .forEach(item -> {
                    AlarmCategory.getChildrenByParent(item.getAlarmCategory())
                            .stream()
                            .map(child -> new AlarmCategoryWithMessageResponseDto(child, null))
                            .forEach(item::addChildren);
//                    List<AlarmCategory> childrenByParent = AlarmCategory.getChildrenByParent(item.getAlarmCategory());
//                    alarmCategoryMessageService.findByAlarmCategoryIn(childrenByParent)
//                            .stream()
//                            .map(alarmCategoryMessage -> new AlarmCategoryWithMessageResponseDto(alarmCategoryMessage.getAlarmCategory(), alarmCategoryMessage))
//                            .forEach(item::addChildren);
                });
        return BaseResponse.onSuccess(collect);
    }

    @Deprecated
    @Operation(summary = "[VALID] [Common] AlarmCategory 로 CategoryMessage 조회 ", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/{alarmCategory}/message") // TODO 문제 발생 응답이 이상함
    public BaseResponse<AlarmCategoryWithMessageResponseDto> getAlarmCategoryMessage(@CurrentMember Member member,
                                                                                                       @PathVariable AlarmCategory alarmCategory) {
        AlarmCategoryMessage alarmCategoryMessage = alarmCategoryMessageService.findByAlarmCategory(alarmCategory);
        return BaseResponse.onSuccess(new AlarmCategoryWithMessageResponseDto(alarmCategory, alarmCategoryMessage));
    }

    @Operation(summary = "[VALID] [Common] ChildrenCategoryId로 AlarmId 조회 / [봉사자] 녹음 2단계 & [청년] 위로 청취 2단계", description = "위로 녹음9 에서 상단에 보여줄 멘트를 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공"),
    })
    @GetMapping("/alarm-category/{childrenAlarmCategory}/detail")
    public BaseResponse<AlarmCategoryMessageResponseDto> getAlarmCategory(@CurrentMember Member member,
                                                                          @PathVariable(value = "childrenAlarmCategory") AlarmCategory alarmCategory) {
        Alarm recommendedAlarm = alarmViewService.findRecommendedAlarm(member.getId(), alarmCategory);
        AlarmCategoryMessage alarmCategoryMessage = alarmCategoryMessageService.findByAlarmCategory(alarmCategory);
        AlarmCategoryMessageResponseDto alarmCategoryMessageResponseDto = new AlarmCategoryMessageResponseDto(recommendedAlarm, alarmCategoryMessage);
        return BaseResponse.onSuccess(alarmCategoryMessageResponseDto);
    }

    @Operation(summary = "[VALID] [봉사자] 녹음 1단계: 녹음 선택 리스트 조회 -> '[Common] ChildrenCategoryId로 AlarmId 조회'로 이동", description = "사용자의 녹음할 리스트를 조회합니다. 정렬은 1. 사용자가 작성하지 않음 2. 데이터 수가 적음 순으로 보여지게 됩니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "조회 성공"),
    })
    @GetMapping("/list/{categoryType}")
    public BaseResponse<List<AlarmCategoryCount>> getAlarmList(@CurrentMember Member member,
                                                               @PathVariable CategoryType categoryType) {
        // TODO 여기에 메시지가 제공되어야함
        List<AlarmCategoryCount> collect = alarmViewService.findUserCategoryCount(member.getId(), categoryType);
        List<AlarmCategory> alarmCategories = collect.stream()
                .map(AlarmCategoryCount::getAlarmCategory)
                .collect(Collectors.toList());

        Map<AlarmCategory, AlarmCategoryMessage> map = alarmCategoryMessageService.findByAlarmCategoryIn(alarmCategories)
                .stream()
                .collect(Collectors.toMap(AlarmCategoryMessage::getAlarmCategory, alarmCategoryMessage -> alarmCategoryMessage));
        collect
                .stream()
                .forEach(item -> {
                    AlarmCategoryMessage alarmCategoryMessage = map.get(item.getAlarmCategory());
                    item.setTitle(alarmCategoryMessage.getTitle());
                });

        return BaseResponse.onSuccess(collect);
    }

    @Operation(summary = "[VALID] [봉사자] 동기부여 1단계: 북두칠성 조회 API", description = "사용자의 북두칠성을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AlarmCategoryWithMessageResponseDto.class)))
    })
    @GetMapping("/this-week")
    public BaseResponse<List<AlarmCategoryWithMessageResponseDto>> getDistinctAlarmTypesThisWeek(@CurrentMember Member member) {

        // 이번 주에 사용된 알람의 부모 카테고리 조회
        Set<AlarmCategory> distinctCreatedCategories = alarmViewService.findDistinctCreatedCategories(member.getId());
        List<AlarmCategoryWithMessageResponseDto> collect = alarmCategoryMessageService.findByAlarmCategoryIn(distinctCreatedCategories.stream().toList())
                .stream()
                .map(alarmCategoryMessage -> new AlarmCategoryWithMessageResponseDto(alarmCategoryMessage.getAlarmCategory(), alarmCategoryMessage))
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(collect);
    }

    // TODO 위 아래 중복 로직임
    @Operation(summary = "[VALID] [봉사자] 동기부여 4단계: 카테고리 목록 조회", description = "위로 목록을 조회합니다. 이후 '[청년] 청취 1단계'로 이동합니다. ")
    @GetMapping("/alarm-category/")
    public BaseResponse<List<AlarmCategoryWithMessageResponseDto>> getAlarmCategoryList(@CurrentMember Member member) {
        List<AlarmCategoryWithMessageResponseDto> list = alarmCategoryMessageService.findByAlarmCategoryIn(ROOT_CATEGORIES)
                .stream()
                .map(alarmCategoryMessage -> new AlarmCategoryWithMessageResponseDto(alarmCategoryMessage.getAlarmCategory(), alarmCategoryMessage))
                .collect(Collectors.toList());
        return BaseResponse.onSuccess(list);
    }
}
