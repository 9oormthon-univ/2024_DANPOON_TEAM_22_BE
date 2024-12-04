package naeilmolae.domain.weather.domain;

import jakarta.persistence.*;
import naeilmolae.global.common.base.BaseEntity;

@Entity
public class Weather extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grid_id")
    private Grid grid;

    private WeatherCategory category;
    private Double value;
}
