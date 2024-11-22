package naeilmolae.domain.member.client;

import naeilmolae.domain.member.dto.client.KakaoResponse;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AuthErrorStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoMemberClient {

    private final WebClient webClient;


    public KakaoMemberClient(WebClient.Builder webClientBuilder ) {
        this.webClient = webClientBuilder
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .build();
    }

    public String getClientId(final String accessToken) {
        KakaoResponse response = webClient.get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoResponse.class)
                .block();

        if(response == null)
            throw new RestApiException(AuthErrorStatus.FAILED_SOCIAL_LOGIN);

        return response.getId();
    }
}
