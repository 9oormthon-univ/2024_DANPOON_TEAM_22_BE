package naeilmolae.domain.chatgpt.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChatGptResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;

        @Data
        public static class Message {
            private String role;
            private String content;
        }
    }
}