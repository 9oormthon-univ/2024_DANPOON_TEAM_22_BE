package naeilmolae.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.chatgpt.dto.request.ChatGptRequest;
import naeilmolae.domain.chatgpt.dto.response.ChatGptResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final WebClient webClient;

    public Boolean checkForOffensiveLanguage(String sentence) {
        // 요청 데이터 생성
        ChatGptRequest request = new ChatGptRequest(
                "gpt-4o-mini",
                List.of(
                        // 시스템 메시지
                        // 문장이 욕설을 포함하는지 확인하는 도우미
                        // 문자엥 부적절한 언어가 포함되어 있는지 확인하고, 포함되어 있다면 'true'를, 그렇지 않다면 'false'를 응답하세요.
                        // todo: 스크립트 잘 작성
                        new ChatGptRequest.ChatGptMessage("system",
                                "You are an assistant that checks whether a sentence contains offensive " +
                                        "language. Respond only with 'true' if the sentence contains offensive language and 'false' otherwise."),
                        new ChatGptRequest.ChatGptMessage("user", "Is this sentence offensive: '" + sentence + "'?")
                ),
                10,
                0.0
        );

        // API 호출
        ChatGptResponse response =  webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGptResponse.class)
                .block();

        // 응답 반환
        // 응답에서 "true"(부적절) 또는 "false"(적절) 추출
        String content = response.getChoices().get(0).getMessage().getContent().trim().toLowerCase();
        return "true".equals(content);
    }
}

