package naeilmolae;

import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @BeforeAll에서 static 없이 사용할 수 있도록 설정
public class BaseTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    protected static String accessToken;

    @BeforeAll
    void setUp() {
        if (accessToken == null) {
            registerAndLogin();
        }
    }

    private void registerAndLogin() {
        String uriString = UriComponentsBuilder.fromUriString("/api/v1/auth/login")
                .queryParam("accessToken", "123")
                .queryParam("loginType", "ANOYMOUS")
                .toUriString();

        ResponseEntity<MemberLoginResponseDto> signUpResponse
                = restTemplate.postForEntity(uriString, null, MemberLoginResponseDto.class);

        accessToken = signUpResponse.getBody().getAccessToken();
    }

    protected HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        return headers;
    }
}