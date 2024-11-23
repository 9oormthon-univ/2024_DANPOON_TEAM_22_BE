package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;
import naeilmolae.domain.alarm.dto.AlarmCategoryCount;
import naeilmolae.domain.alarm.repository.AlarmCategoryRepository;
import naeilmolae.domain.alarm.repository.AlarmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmViewService {
    private final AlarmService alarmService;
    private final AlarmCategoryRepository alarmCategoryRepository;
    private final AlarmRepository alarmRepository;

    // 위로 카테고리 알람 조회
    public List<AlarmCategory> findComfortAlarmCategoryList() {
        return alarmCategoryRepository.findByCategoryTypeAndParentIsNull(CategoryType.COMFORT);
    }

    // 추천 알람 조회
    public Alarm findRecommendedAlarm(Long memberId, Long parentCategoryId) {
        List<Alarm> alarmsByParentCategoryId = alarmRepository.findAlarmsByParentCategoryId(parentCategoryId);
        if (alarmsByParentCategoryId.isEmpty()) {
            return null; // 리스트가 비어있으면 null 반환
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(alarmsByParentCategoryId.size());
        return alarmsByParentCategoryId.get(randomIndex);
    }



    // 이번 주에 사용된 알람의 부모 카테고리 조회
    public List<AlarmCategory> findDistinctCreatedCategories(Long memberId) {
        List<Alarm> usedAlarmInThisWeek = alarmService.findUsedAlarmInThisWeek(memberId);
        return usedAlarmInThisWeek.stream()
                .map(Alarm::getAlarmCategory)
                .map(AlarmCategory::getParent)
                .distinct()
                .collect(Collectors.toList());
    }

    // VoiceFile이 부족한 부모 카테고리 조회
    public List<AlarmCategoryCount> findUserCategoryCount(Long memberId, CategoryType categoryType) {
//        LocalDateTime startOfWeek = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//        LocalDateTime endOfWeek = LocalDateTime.now();

        // TODO 1. 가장 적게 작성된 AlarmCateogry 나열
        // TODO 2. 그 중에 사용자가 작성한 거는 뒤로
//        List<Object[]> parentCategoriesWithVoiceFileCount = alarmCategoryRepository.findParentCategoriesWithVoiceFileCount(categoryType);
//        List<AlarmCategoryCount> collect = parentCategoriesWithVoiceFileCount
//                .stream()
//                .sorted(Comparator.comparingLong(result -> (Long) result[1]))
//                .map(result -> {
//                    AlarmCategory parentCategory = (AlarmCategory) result[0];
//                    Long voiceFileCount = (Long) result[1];
//                    return new AlarmCategoryCount(parentCategory.getId(), parentCategory.getTitle(), voiceFileCount);
//                })
//                .collect(Collectors.toList());

        return alarmCategoryRepository.findByCategoryTypeAndParentIsNull(categoryType)
                .stream()
                .map(parentCategory -> {
                    return new AlarmCategoryCount(parentCategory.getId(), parentCategory.getTitle(), 0L);
                }).toList();
    }

}
