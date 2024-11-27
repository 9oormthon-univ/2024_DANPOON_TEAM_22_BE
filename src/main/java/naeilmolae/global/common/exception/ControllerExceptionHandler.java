package naeilmolae.global.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClient;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private final WebClient webClient;
    private final String discordWebhookUrl;

    // Webhook URL은 application.yml 또는 application.properties에서 주입
    public ControllerExceptionHandler(@Value("${discord.webhook.url}") String discordWebhookUrl, WebClient.Builder webClientBuilder) {
        this.discordWebhookUrl = discordWebhookUrl;
        this.webClient = webClientBuilder.build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.error("An error occurred: {}", e.getMessage(), e);

        //디스코드 알림 전송
        sendErrorToDiscord(String.format("An error occurred: %s\n%s", e.getMessage(), e.toString()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void sendErrorToDiscord(String message) {
        try {
            webClient.post()
                    .uri(discordWebhookUrl)
                    .bodyValue(new DiscordMessage(message))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception ex) {
            logger.error("Failed to send error to Discord: {}", ex.getMessage());
        }
    }

    // 디스코드 메시지에 사용할 간단한 클래스
    private static class DiscordMessage {
        private final String content;

        public DiscordMessage(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}