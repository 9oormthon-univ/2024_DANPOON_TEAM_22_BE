package naeilmolae.domain.weather.repository;

import naeilmolae.domain.weather.domain.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Grid, Long> {
}
