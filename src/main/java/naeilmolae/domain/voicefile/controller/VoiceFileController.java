package naeilmolae.domain.voicefile.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.chatgpt.service.ChatGptService;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.request.UploadContentRequestDto;
import naeilmolae.domain.voicefile.dto.response.VoiceFileMetaResponseDto;
import naeilmolae.domain.voicefile.service.VoiceFileService;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import naeilmolae.global.util.S3FileComponent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voicefiles")
public class VoiceFileController {
    private final VoiceFileService voiceFileService;
    private final S3FileComponent s3FileComponent;


    @Operation(summary = "[VALID] [봉사자] 녹음 3-2단계: 작성한 스크립트 저장", description = "사용자가 작성한 스크립트를 저장합니다. GPT에게 검증을 받으며 부적절한 스크립트 제공시 예외가 발생할 수 있습니다. [NOTICE] 에러 응답은 추후에 추가하겠습니다.")
    @PostMapping("/{alarmId}/self")
    public BaseResponse<VoiceFileMetaResponseDto> uploadContent(@CurrentMember Member member,
                                                                @PathVariable(value = "alarmId") Long alarmId,
                                                                @RequestBody UploadContentRequestDto uploadContentRequestDto) {
        VoiceFile voiceFile = voiceFileService.saveContent(member.getId(),
                alarmId,
                uploadContentRequestDto.content());

        return BaseResponse.onSuccess(VoiceFileMetaResponseDto.fromEntity(voiceFile)); // TODO GPT에게 첨삭받고 응답으로 내려주어야 한다.
    }
}
