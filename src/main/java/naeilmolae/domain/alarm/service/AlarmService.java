package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.dto.response.AlarmCategoryResponseDto;
import naeilmolae.domain.alarm.repository.AlarmCategoryRepository;
import naeilmolae.domain.alarm.repository.AlarmRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final AlarmCategoryRepository alarmCategoryRepository;

    // 알람 조회
    public Alarm findById(Long id) {
        return alarmRepository.findById(id)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    // 이번 주에 사용된 알람 조회
    public List<Alarm> findUsedAlarmInThisWeek(Long memberId) {
        LocalDateTime startOfWeek = LocalDateTime
                .now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return alarmRepository.findByMemberIdAndBetween(memberId,
                startOfWeek,
                LocalDateTime.now());
    }

    // 알람 카테고리 ID로 알람 조회
    public Alarm findByAlarmCategoryId(Long alarmCategoryId) {
        return alarmRepository.findByAlarmCategoryId(alarmCategoryId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._BAD_REQUEST));
    }

    public List<AlarmCategory> findParentCateories() {
        return alarmCategoryRepository.findByParentIsNull();
    }

}
