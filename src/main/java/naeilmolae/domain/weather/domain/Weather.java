package naeilmolae.domain.weather.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weather extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grid_id")
    private Grid grid;

    private WeatherCategory category;
    private Double value;

    public Weather(Grid grid, WeatherCategory category, Double value) {
        this.grid = grid;
        this.category = category;
        this.value = value;
    }
}
