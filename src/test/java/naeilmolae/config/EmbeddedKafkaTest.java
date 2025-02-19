package naeilmolae.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import naeilmolae.domain.voicefile.dto.request.AnalysisRequestDto;
import naeilmolae.domain.voicefile.dto.response.AnalysisResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@EmbeddedKafka(partitions = 3,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:10000"
        },
        ports = { 10000 })
class EmbeddedKafkaTest {

    @Autowired
    KafkaTemplate<String, AnalysisRequestDto> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${kafka.topic.analysis.request}")
    private String REQUEST_TOPIC;

    @Test
    void test() throws Exception {
        // given
        AnalysisRequestDto event = new AnalysisRequestDto("fileUrl", "content");
        kafkaTemplate.setDefaultTopic(REQUEST_TOPIC);
        kafkaTemplate.send(MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.KEY, "1")
                .setHeader("requiredResponseType", AnalysisResponseDto.class.getName())
                .build());
        // when
        Thread.sleep(2000);

        // then

    }
    @TestConfiguration
    static class TestConfig {
        @Bean
        public KafkaConsumer kafkaConsumer() {
            return new KafkaConsumer();
        }
    }

    static class KafkaConsumer {
        private List<AnalysisResponseDto> eventRepo = new ArrayList<>();

        @KafkaListener(topics = "${kafka.topic.analysis.response}", groupId = "testGroup")
        public void handleAnalysisResponse(
                @Payload AnalysisResponseDto analysisResponseDto,
                @Header(KafkaHeaders.RECEIVED_KEY) String key) {
            eventRepo.add(analysisResponseDto);
        }

        public List<AnalysisResponseDto> getEventRepo() {
            return eventRepo;
        }
    }

}