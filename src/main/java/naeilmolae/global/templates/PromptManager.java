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

    //부적절하다면 문장 작성자에게 왜 부적절한지 높임말로 reason에 간결히 작성해줘. reason 이 없어도 null로 채워줘.
    public String createCheckForOffensiveLanguagePrompt( String situation, String statement ) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
                ## 명령 
                주어진 문장이 특정 상황에 처한 사람에게 적절한 응원이나 표현인지 판단해 주고, 
                욕설이 있는게 아니라면 너무 엄격하지 않았으면해.
                reason은 그냥 null로 줘도 돼.
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