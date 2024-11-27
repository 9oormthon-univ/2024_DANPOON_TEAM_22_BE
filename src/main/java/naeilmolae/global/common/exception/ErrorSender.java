package naeilmolae.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class ErrorSender {

    private static final Logger logger = LoggerFactory.getLogger(ErrorSender.class);

    private final WebClient webClient;
    private final String discordWebhookUrl;

    // Webhook URL은 application.yml 또는 application.properties에서 주입
    public ErrorSender(@Value("${discord.webhook.url}") String discordWebhookUrl, WebClient.Builder webClientBuilder) {
        this.discordWebhookUrl = discordWebhookUrl;
        this.webClient = webClientBuilder.build();
    }


    public void sendErrorToDiscord(String message) {
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