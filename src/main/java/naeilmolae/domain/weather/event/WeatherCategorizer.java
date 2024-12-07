package naeilmolae.domain.weather.event;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.weather.domain.Weather;
import naeilmolae.domain.weather.domain.WeatherCategory;

import java.util.List;

public interface WeatherCategorizer {
    AlarmCategory categorize(List<Weather> weatherList);
}
