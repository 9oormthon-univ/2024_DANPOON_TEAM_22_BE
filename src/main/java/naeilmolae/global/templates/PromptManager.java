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

    //todo: 상황 별 validation이 안되는 듯
    public String createCheckForOffensiveLanguagePrompt( String situation ) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
                You are an assistant that evaluates whether a given script is appropriate for a specific situation. 
                Consider the following factors:
                - Relevance: Does the script directly address the given situation?
                - Appropriateness: Does the script avoid offensive, negative, or harmful language?
                - Clarity: Is the script clear and easy to understand?
        
                ### Situation
                \'%s\' 라는 스크립트를 작성하는 상황
                """.formatted(situation),
                """
                Respond with 'true' if the script is appropriate for the situation based on the above criteria. 
                Respond with 'false' otherwise.
                """
        );
    }

}