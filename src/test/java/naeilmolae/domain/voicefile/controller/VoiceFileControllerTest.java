package naeilmolae.domain.voicefile.controller;

import naeilmolae.config.BaseTest;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.voicefile.dto.response.RetentionDto;
import naeilmolae.domain.voicefile.repository.ProvidedFileRepository;
import naeilmolae.domain.voicefile.repository.VoiceFileRepository;
import naeilmolae.global.common.base.BaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class VoiceFileControllerTest extends BaseTest {
    @Autowired
    VoiceFileRepository voiceFileRepository;
    @Autowired
    ProvidedFileRepository providedFileRepository;

    String PREFIX = "/api/v1/voicefiles";

    @Test
    void retention_성공() {
        // when
        String uriString = UriComponentsBuilder.fromUriString(PREFIX + "/retention").toUriString();
        BaseResponse<RetentionDto> body = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(getAuthHeaders(Role.HELPER)),
                new ParameterizedTypeReference<BaseResponse<RetentionDto>>() {
                }
        ).getBody();
        RetentionDto result = body.getResult();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getVoiceCount()).isEqualTo(3);
        assertThat(result.getThanksCount()).isEqualTo(2);
        assertThat(result.getMessageCount()).isEqualTo(2);
    }
}