package naeilmolae.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.domain.AlarmExample;
import naeilmolae.domain.alarm.repository.AlarmExampleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmExampleService {
    private final AlarmExampleRepository alarmExampleRepository;

    public AlarmExample findAllByAlarmId(Long alarmId) {
        List<AlarmExample> allByAlarmId = alarmExampleRepository.findAllByAlarmId(alarmId);

        if (allByAlarmId.isEmpty()) {
            throw new IllegalArgumentException("No alarm examples found for alarmId: " + alarmId);
        }

        return allByAlarmId.stream()
                .skip(ThreadLocalRandom.current().nextInt(allByAlarmId.size()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unexpected error when selecting random alarm example"));
    }
}
