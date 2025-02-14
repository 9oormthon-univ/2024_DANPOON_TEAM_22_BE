package naeilmolae.domain.voicefile.service;

import naeilmolae.global.common.exception.RestApiException;
import org.aspectj.lang.annotation.RequiredTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class VoiceFileServiceTest {

    @Autowired
    private VoiceFileService voiceFileService;

    @Test
    void verifyUserFile_success() {
        voiceFileService.verifyContent("아침 인사 메시지", "안녕하세요. 오늘도 좋은 하루 되세요.");

    }
    @Test
    void verifyUserFile_fail_201() {
        try {
            voiceFileService.verifyContent("잠자기 전에 ", "안녕하세요. 오늘도 좋은 하루 되세요.");
        } catch (RestApiException e) {
            assertEquals("ANALYSIS200", e.getErrorCode().getCode());
        }
    }
    @Test
    void verifyUserFile_fail_202() {
        try {
            voiceFileService.verifyContent("잠자기 전에 ", "죽어버려 정신나간 녀석. 너는 사회의 악이야.");
        } catch (RestApiException e) {
            assertEquals("ANALYSIS201", e.getErrorCode().getCode());
        }
    }
}