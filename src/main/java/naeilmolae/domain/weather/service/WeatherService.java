package naeilmolae.domain.weather.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.weather.domain.Grid;
import naeilmolae.domain.weather.domain.Weather;
import naeilmolae.domain.weather.domain.WeatherCategory;
import naeilmolae.domain.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;

    private static final String API_URL =
            "https://apihub.kma.go.kr/api/typ02/openApi/VilageFcstInfoService_2.0/getUltraSrtNcst";

    @Value("${weather.api.key}")
    private String apiKey;

    /**
     * 날씨 정보를 가져와서 저장합니다.
     * @param grid
     * @param localDateTime
     * @return
     */
    // TODO 테스트 해야함
    @Transactional
    public List<Weather> requestWeatherData(Grid grid, LocalDateTime localDateTime) {
        String url = buildUrl(grid, localDateTime);
        String response = restTemplate.getForObject(url, String.class);

        List<Weather> weathers = parseWeatherData(response, grid);
        return weatherRepository.saveAll(weathers);
    }

    /**
     * API 요청 URL을 생성합니다.
     * @param grid
     * @param dateTime
     * @return
     */
    private String buildUrl(Grid grid, LocalDateTime dateTime) {
        dateTime = dateTime.withMinute(0);

        String baseDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = dateTime.format(DateTimeFormatter.ofPattern("HHmm"));

        return String.format(
                "%s?pageNo=1&numOfRows=1000&dataType=XML&base_date=%s&base_time=%s&nx=%d&ny=%d&authKey=%s",
                API_URL, baseDate, baseTime, grid.getX(), grid.getY(), apiKey
        );
    }

    /**
     * XML 데이터를 파싱하여 Weather 객체 리스트를 반환합니다.
     * @param xmlData
     * @param grid
     * @return
     */
    private List<Weather> parseWeatherData(String xmlData, Grid grid) {
        List<Weather> weatherList = new ArrayList<>();
        try {
            // XML 파싱 준비
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlData);
            document.getDocumentElement().normalize();

            // <item> 태그 추출
            NodeList itemList = document.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);

                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) itemNode;

                    // XML 요소 값 추출
                    String category = element.getElementsByTagName("category").item(0).getTextContent();
                    double value = Double.parseDouble(element.getElementsByTagName("obsrValue").item(0).getTextContent());

                    // Weather 객체 생성
                    Weather weather = new Weather(grid, WeatherCategory.valueOf(category), value);

                    weatherList.add(weather);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
        return weatherList;
    }
}
