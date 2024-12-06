package naeilmolae.domain.weather.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.weather.domain.Grid;
import naeilmolae.domain.weather.dto.GridDto;
import naeilmolae.domain.weather.repository.GridRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GridErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GridService {
    private final GridRepository gridRepository;
    private final RestTemplate restTemplate;

    @Value("${kma.key}")
    private String apiKey;

    public Grid findGridByPoint(String x, String y) {
        return gridRepository.findByPoint(x, y).orElseThrow(() -> new IllegalArgumentException("해당 좌표를 찾을 수 없습니다."));
    }

    /**
     * 위도, 경도를 받아서 그리드 좌표를 반환합니다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 그리드 좌표
     */
    // TODO 테스트 해야함
    @Transactional
    public Grid getGridCoordinates(Double latitude, Double longitude) { // 위도, 경도
        String apiUrl = String.format(
                "https://apihub.kma.go.kr/api/typ01/cgi-bin/url/nph-dfs_xy_lonlat?lon=%f&lat=%f&help=0&authKey=%s",
                longitude, latitude, apiKey
        );

        String response = restTemplate.getForObject(apiUrl, String.class);
        GridDto gridDto = parseGridCoordinates(response);


        Optional<Grid> opGrid = gridRepository.findByPoint(gridDto.getX().toString(), gridDto.getY().toString());
        if (opGrid.isPresent()) {
            return opGrid.get();
        } else {
            return gridRepository.save(new Grid(gridDto.getX(), gridDto.getY()));
        }

    }


    /**
     * 응답 데이터를 받아 X, Y 좌표를 파싱합니다.
     *
     * @param response API 응답 문자열
     * @return X, Y 좌표 배열 (int[0]: X, int[1]: Y)
     * @throws IllegalArgumentException 응답 형식이 잘못된 경우 예외 발생
     */
    // TODO Converter 로 변경하면 어떨까
    private GridDto parseGridCoordinates(String response) {
        if (response == null || response.isEmpty()) {
            throw new RestApiException(GridErrorCode._NO_CONTENT);
        }

        // 응답 데이터 줄바꿈으로 분리
        String[] lines = response.split("\n");

        // 마지막 줄을 읽어 ','로 분리
        try {
            String[] values = lines[lines.length - 1].trim().split(",");
            if (values.length < 4) {
                throw new RestApiException(GridErrorCode._PARSE_ERROR);
            }

            // X, Y 값 추출 (세 번째와 네 번째 값)
            Integer x = Integer.parseInt(values[2].trim());
            Integer y = Integer.parseInt(values[3].trim());

            return new GridDto(x, y);

        } catch (Exception e) {
            throw new RestApiException(GridErrorCode._ERROR);
        }
    }


}
