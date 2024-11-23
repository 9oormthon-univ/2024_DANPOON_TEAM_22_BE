package naeilmolae.domain.chatgpt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.chatgpt.service.ChatGptService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChatGpt API", description = "소셜 로그인, 토큰 발급/재발급, 로그아웃 등 인증 관련 작업을 수행하는 API")
@Deprecated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatgpt")
public class ChatGptController {

    private final ChatGptService chatGptService;

    @PostMapping("/check-offensive")
    @Operation(summary = "부적절 문장 검증 API"
            , description = "상황에 부적절한 문장인지 검증해서 Boolean 값으로 리턴하는 API입니다. " +
            "상황에 부적절한 문장이라면 true, 아니라면 false를 리턴합니다." +
            "테스트용 api이기 \'우산을 챙기라는 말을 해주세요\'라는 상황으로 고정되어 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "검증 성공")
    })
    public Boolean checkOffensiveLanguage(@RequestBody String sentence) {
        return chatGptService.etCheckScriptRelevancePrompt(sentence, "우산 챙기라는 말을 해주세요");
    }
}
