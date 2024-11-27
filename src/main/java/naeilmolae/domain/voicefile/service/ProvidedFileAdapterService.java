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
    private final VoiceFileService voiceFileService;

    public Set<Long> findAlarmIdsProvidedThisWeek(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDateTime startDay = now.minusDays(7).atStartOfDay();
        LocalDateTime endDay = LocalDateTime.now();
        List<Long> alarmIdsByConsumerId = voiceFileService.findAlarmIdsByMemberIdAndBetween(memberId, startDay, endDay);
        return Set.copyOf(alarmIdsByConsumerId);
    }
}
