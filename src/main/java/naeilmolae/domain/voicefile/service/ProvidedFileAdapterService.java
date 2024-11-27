package naeilmolae.domain.voicefile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProvidedFileAdapterService {
    private final ProvidedFileService providedFileService;

    public Set<Long> findAlarmIdsProvidedThisWeek(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDateTime startDay = now.atStartOfDay();
        LocalDateTime endDay = now.plusDays(7).atStartOfDay();
        List<Long> alarmIdsByConsumerId = providedFileService.findAlarmIdsByConsumerId(memberId, startDay, endDay);
        return Set.copyOf(alarmIdsByConsumerId);
    }
}
