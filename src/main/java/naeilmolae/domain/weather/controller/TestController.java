package naeilmolae.domain.weather.controller;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.weather.domain.Grid;
import naeilmolae.domain.weather.domain.Weather;
import naeilmolae.domain.weather.dto.GridDto;
import naeilmolae.domain.weather.dto.WeatherDto;
import naeilmolae.domain.weather.service.GridService;
import naeilmolae.domain.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/weather/grid")
@RequiredArgsConstructor
public class TestController {
    private final GridService gridService;
    private final WeatherService weatherService;

    /**
     * 위도, 경도를 받아서 그리드 좌표를 반환합니다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 그리드 좌표
     */
    @GetMapping
    public ResponseEntity<Grid> getGridCoordinates(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        Grid grid = gridService.getGridCoordinates(latitude, longitude);
        return ResponseEntity.ok(grid);
    }

    @PostMapping("/data")
    public ResponseEntity<List<WeatherDto>> requestWeatherData(
            @RequestParam("gridX") Integer gridX,
            @RequestParam("gridY") Integer gridY,
            @RequestParam("dateTime") String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
        GridDto gridDto = new GridDto(gridX, gridY); // Grid 객체 생성

        List<WeatherDto> collect = weatherService.requestWeatherData(gridDto, localDateTime)
                .stream()
                .map(WeatherDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }
}
