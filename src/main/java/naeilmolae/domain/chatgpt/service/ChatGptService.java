package naeilmolae.domain.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.chatgpt.dto.ScriptValidationResponseDto;
import naeilmolae.global.infrastructure.ai.OpenAiApiClient;
import naeilmolae.global.infrastructure.ai.dto.request.ChatGptRequest;
import naeilmolae.global.infrastructure.ai.dto.response.ChatGptResponse;
import naeilmolae.global.templates.PromptManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ChatGptService {

    private final OpenAiApiClient openAiApiClient;
    private final PromptManager promptManager;

    private final String model = "gpt-4o-mini";

    private final String systemRole = "system";

    private final String userRole = "user";
    private final String userPrompt = "Is this sentence offensive: ";

    private final int maxTokens = 100;
    private final double temperature = 0.5;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public ScriptValidationResponseDto getCheckScriptRelevancePrompt(String sentence, String Situation) {

        // 템플릿 생성
        String prompt = promptManager.createCheckForOffensiveLanguagePrompt(Situation, sentence);

        ChatGptResponse response = openAiApiClient.sendRequestToModel(
                model,
                List.of(
                        new ChatGptRequest.ChatGptMessage(systemRole, prompt)
//                        new ChatGptRequest.ChatGptMessage(userRole, userPrompt + "'" + sentence + "'?")
                ),
                maxTokens,
                temperature
        );

        String content = response.getChoices().get(0).getMessage().getContent().trim().toLowerCase();
        System.out.println(content);
        try {
            return objectMapper.readValue(content, ScriptValidationResponseDto.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

