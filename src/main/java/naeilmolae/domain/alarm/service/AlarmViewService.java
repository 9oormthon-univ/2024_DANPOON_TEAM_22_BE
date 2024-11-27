package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;
import naeilmolae.domain.alarm.dto.AlarmCategoryCount;
import naeilmolae.domain.alarm.dto.response.AlarmResponseDto;
import naeilmolae.domain.alarm.repository.AlarmCategoryMessageRepository;
import naeilmolae.domain.alarm.repository.AlarmRepository;
import naeilmolae.domain.voicefile.service.ProvidedFileAdapterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmViewService {
    private final AlarmService alarmService;
    private final AlarmCategoryMessageService alarmCategoryMessageService;

    private final ProvidedFileAdapterService providedFileAdapterService;


    // 추천 알람 조회
    public Alarm findRecommendedAlarm(Long memberId, AlarmCategory parentCategory) {
        List<AlarmCategory> childCategories = AlarmCategory.getByParent(parentCategory);
        List<Alarm> alarmsByParentCategoryId = alarmService.findByCategories(childCategories);
        if (alarmsByParentCategoryId.isEmpty()) {
            return null; // 리스트가 비어있으면 null 반환
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(alarmsByParentCategoryId.size());
        return alarmsByParentCategoryId.get(randomIndex);
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

    // VoiceFile이 부족한 부모 카테고리 조회
    public List<AlarmCategoryCount> findUserCategoryCount(Long memberId, CategoryType categoryType) {
        // TODO 1. 가장 적게 작성된 AlarmCateogry 나열
        // TODO 2. 그 중에 사용자가 작성한 거는 뒤로

        List<AlarmCategory> collect = AlarmCategory.ROOT_CATEGORIES
                .stream()
                .filter(item -> item.getCategoryType().equals(categoryType))
                .collect(Collectors.toList());

        return alarmService.findByCategories(collect)
                .stream()
                .map(AlarmCategoryCount::new)
                .collect(Collectors.toList());
    }
}
