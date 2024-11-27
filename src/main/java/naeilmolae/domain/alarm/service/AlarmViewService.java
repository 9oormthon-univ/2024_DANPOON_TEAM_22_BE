package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;
import naeilmolae.domain.alarm.dto.AlarmCategoryCount;
import naeilmolae.domain.voicefile.service.ProvidedFileAdapterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmViewService {
    private final AlarmService alarmService;

    private final ProvidedFileAdapterService providedFileAdapterService;


    // 알림 조회
    public Alarm findRecommendedAlarm(Long memberId, AlarmCategory childAlarmCategory) {
        return alarmService.findByAlarmCategory(childAlarmCategory);
    }


    // 이번 주에 사용된 알람의 부모 카테고리 조회
    public Set<AlarmCategory> findDistinctCreatedCategories(Long memberId) {
        // 이번주에 사용된 AlarmId 조회
        Set<Long> alarmIds
                = providedFileAdapterService.findAlarmIdsProvidedThisWeek(memberId);
        return alarmService.findByIdIn(alarmIds)
                .stream()
                .map(Alarm::getAlarmCategory)
                .map(AlarmCategory::getParent)
                .collect(Collectors.toSet());
    }

    // 카테고리 순서 맞추기
    public List<AlarmCategoryCount> findUserCategoryCount(Long memberId, CategoryType categoryType) {
        // TODO 1. 사용자가 작성한 알람 조회
        Set<AlarmCategory> userUsed = this.findDistinctCreatedCategories(memberId)
                .stream()
                .filter(item -> item.getCategoryType().equals(categoryType))
                .collect(Collectors.toSet());

        // TODO 2. 순서 맞추기
        List<AlarmCategoryCount> collect = AlarmCategory.ROOT_CATEGORIES
                .stream()
                .filter(item -> item.getCategoryType().equals(categoryType))
                .map(AlarmCategoryCount::new)
                .collect(Collectors.toList());

        // collect 리스트를 순회하면서 userUsed에 포함된 요소는 맨 뒤로 이동
        List<AlarmCategoryCount> sortedCollect = new ArrayList<>();

        // userUsed에 포함되지 않은 항목 먼저 추가
        collect.stream()
                .filter(item -> !userUsed.contains(item.getAlarmCategory()))
                .forEach(sortedCollect::add);

        // userUsed에 포함된 항목을 뒤에 추가
        collect.stream()
                .filter(item -> userUsed.contains(item.getAlarmCategory()))
                .forEach(item -> {
                    item.setUsed();         // isUsed 값을 true로 설정
                    sortedCollect.add(item); // sortedCollect에 추가
                });

        return sortedCollect;
    }
}
