package naeilmolae.domain.weather.repository;

import naeilmolae.domain.weather.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
