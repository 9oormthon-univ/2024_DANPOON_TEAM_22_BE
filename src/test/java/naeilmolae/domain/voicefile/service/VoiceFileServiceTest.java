package naeilmolae.domain.voicefile.service;

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
    void verifyUserFile() {
        voiceFileService.verifyContent("잠자기 전에 ", "안녕하세요. 오늘도 좋은 하루 되세요.");
    }
}