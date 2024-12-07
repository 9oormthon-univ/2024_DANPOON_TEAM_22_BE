package naeilmolae.domain.weather.dto;

import lombok.Getter;
import naeilmolae.domain.weather.domain.Grid;
import naeilmolae.domain.weather.domain.Weather;
import naeilmolae.domain.weather.domain.WeatherCategory;

@Getter
public class WeatherDto {
    private Long gridId;
    private WeatherCategory category;
    private Double value;

    public WeatherDto(Weather weather) {
        this.gridId = weather.getGrid().getId();
        this.category = weather.getCategory();
        this.value = weather.getValue();
    }
}
