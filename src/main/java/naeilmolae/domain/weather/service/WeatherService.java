package naeilmolae.domain.weather.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.weather.domain.Grid;
import naeilmolae.domain.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.transfer}")
    private String TRANSFER_URL;

    public Grid getGridCoordinates(Double latitude, Double longitude) {
        String apiUrl = String.format(
                "https://apihub.kma.go.kr/api/typ01/cgi-bin/url/nph-dfs_xy_lonlat?lon=%f&lat=%f&help=0&authKey=cJGQY1PQTnuRkGNT0H57zQ",
                longitude, latitude
        );

        String response = restTemplate.getForObject(apiUrl, String.class);
        return parseGridCoordinates(response);
    }


    /**
     * 응답 데이터를 받아 X, Y 좌표를 파싱합니다.
     *
     * @param response API 응답 문자열
     * @return X, Y 좌표 배열 (int[0]: X, int[1]: Y)
     * @throws IllegalArgumentException 응답 형식이 잘못된 경우 예외 발생
     */
    private Grid parseGridCoordinates(String response) {
        if (response == null || response.isEmpty()) {
            throw new IllegalArgumentException("응답 데이터가 비어있습니다.");
        }

        // 응답 데이터 줄바꿈으로 분리
        String[] lines = response.split("\n");

        // 마지막 줄을 읽어 ','로 분리
        try {
            String[] values = lines[lines.length - 1].trim().split(",");
            if (values.length < 4) {
                throw new IllegalArgumentException("응답 데이터 형식이 올바르지 않습니다.");
            }

            // X, Y 값 추출 (세 번째와 네 번째 값)
            Integer x = Integer.parseInt(values[2].trim());
            Integer y = Integer.parseInt(values[3].trim());

            return new Grid(x, y);

        } catch (Exception e) {
            throw new IllegalArgumentException("좌표를 파싱하는 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
