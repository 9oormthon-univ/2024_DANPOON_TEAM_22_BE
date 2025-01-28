package naeilmolae.domain.member.client;

import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AuthErrorStatus;
import naeilmolae.global.infrastructure.kakao.dto.KakaoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class KakaoMemberClient {

    private final WebClient webClient;


    public KakaoMemberClient(WebClient.Builder webClientBuilder ) {
        this.webClient = webClientBuilder
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .build();
    }

    // 그니까, 이 메서드는 클라이언트의 역할만 하도록 변경해야 해야 함
    public KakaoResponse getkakaoResponse(final String accessToken) {

        try {
            return webClient.get()
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(KakaoResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            // 실패한 응답을 그대로 반환하거나 예외를 던질 수 있도록 설계
            throw e;
        }
//
//
//        if(response == null)
//            throw new RestApiException(AuthErrorStatus.FAILED_SOCIAL_LOGIN);
//
//        return response.getId();
    }
}
