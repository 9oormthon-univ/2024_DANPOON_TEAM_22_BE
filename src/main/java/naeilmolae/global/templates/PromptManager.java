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
                주어진 문장이 특정 상황에 처한 사람에게 적절한 응원이나 표현인지 판단해 주고, 적절하다면 reason은 그냥 null로 줘도 돼.
                적절하지 않다면 상황에 맞지 않은 응원이어서이면 reason에 0을 리턴, 듣기 거북한 표현이 있어서라면 1을 리턴해.
                꼭 제시된 응답형식을 지켜.
                ## 상황
                \'%s\' 
                ## 문장
                \'%s\' 
                """.formatted(situation, statement),
                """
                {"is_proper":<boolean>, "reason": <integer or null>}
                """
        );
    }

    public String createCheckForOffensiveLanguagePrompt2(String stat1, String stat2) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
                ## 명령 
                문장2가 문장1을 따라 읽었는지 확인해줘. 맞다면 is_proper를 true로 reason을 null로, 아니라면 is_proper을 false로 리턴하고 reason을 0으로 리턴해.
                만약에 욕설 및 불쾌한 표현이 포함되어있다면 reason에 1을 리턴하고 부적절한 상황이야. 
                꼭 제시된 응답형식을 지켜.
                ## 문장1
                \'%s\' 
                ## 문장2
                \'%s\' 
                """.formatted(stat1, stat2),
                """
                {"is_proper":<boolean>, "reason": <integer or null>}
                """
        );
    }

}
