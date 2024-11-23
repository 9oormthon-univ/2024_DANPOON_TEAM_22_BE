package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;
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


}
