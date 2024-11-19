package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.alarm.repository.AlarmRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {
    private final AlarmRepository alarmRepository;

    // 알람 조회
    public Alarm findById(Long id) {
        return alarmRepository.findById(id)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

}
