package naeilmolae.domain.weather.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberAdapterService;
import naeilmolae.domain.pushnotification.strategy.impl.OutingNotificationStrategy;
import naeilmolae.domain.weather.domain.Grid;
import naeilmolae.domain.weather.domain.Weather;
import naeilmolae.domain.weather.domain.WeatherCategory;
import naeilmolae.domain.weather.service.GridService;
import naeilmolae.domain.weather.service.WeatherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherFetchingScheduler {
    private final WeatherService weatherService;
    private final GridService gridService;
    private final WeatherCategorizer weatherCategorizer;
    private final MemberAdapterService memberAdapterService;
    private final OutingNotificationStrategy outingNotificationStrategy;

    @Scheduled(cron = "0 * * * * *")
    public void fetchWeatherData() {
        log.info("WeatherFetchingScheduler.fetchWeatherData()");
        List<Grid> grids = gridService.findAll();

        Map<Long, AlarmCategory> map = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        for (Grid grid : grids) {
            List<Weather> weathers = weatherService.requestWeatherData(grid.getX().toString(), grid.getY().toString(), now);
            AlarmCategory categorize = weatherCategorizer.categorize(weathers);
            map.put(grid.getId(), categorize);
        }

        List<Member> allYouthMember = memberAdapterService.getAllYouthMember();
        Map<Long, AlarmCategory> result = new HashMap<>();
        for (Member member : allYouthMember) {
            result.put(member.getId(), map.get(member.getYouthMemberInfo().getGridId()));
        }

        outingNotificationStrategy.updateAlarmCategoryMap(result);
    }
}
