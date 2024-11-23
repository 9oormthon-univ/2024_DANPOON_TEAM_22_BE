package naeilmolae.global.templates;


import org.springframework.stereotype.Component;

@Component
public class PromptManager {

    private final String CheckForOffensiveLanguagePrompt = "You are an assistant that checks whether a sentence contains offensive " +
            "language. Respond only with 'true' if the sentence contains offensive language and 'false' otherwise.";


    public String createPrompt(String request, String responseFormat) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(request, responseFormat);
    }

    public String createCheckForOffensiveLanguagePrompt( String situation, String statement ) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
                ## 명령 
                주어진 문장이 특정 상황에 처한 사람에게 적절한 응원이나 표현인지 판단해 주고, 부적절하다면 reason 필드를 추가하여 형식에 맞게 출력해줘. reason 이 없어도 null로 채워줘
                ## 상황
                \'%s\' 
                ## 문장
                \'%s\' 
                """.formatted(situation, statement),
                """
                {"is_proper":<boolean>, "reason": <부적절한 이유>}
                """
        );
    }

}