package naeilmolae.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.global.infrastructure.ai.OpenAiApiClient;
import naeilmolae.global.infrastructure.ai.dto.request.ChatGptRequest;
import naeilmolae.global.infrastructure.ai.dto.response.ChatGptResponse;
import naeilmolae.global.templates.PromptManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final OpenAiApiClient openAiApiClient;
    private final PromptManager promptManager;

    private final String model = "gpt-4o-mini";

    private final String systemRole = "system";

    private final String userRole = "user";
    private final String userPrompt = "Is this sentence offensive: ";

    private final int maxTokens = 10;
    private final double temperature = 0.0;


    public Boolean etCheckScriptRelevancePrompt(String sentence, String Situation) {

        // 템플릿 생성
        String prompt = promptManager.createCheckForOffensiveLanguagePrompt(Situation);

        ChatGptResponse response = openAiApiClient.sendRequestToModel(
                model,
                List.of(
                        new ChatGptRequest.ChatGptMessage(systemRole, prompt),
                        new ChatGptRequest.ChatGptMessage(userRole, userPrompt + "'" + sentence + "'?")
                ),
                maxTokens,
                temperature
        );

        String content = response.getChoices().get(0).getMessage().getContent().trim().toLowerCase();
        return "true".equals(content);


    }
}

