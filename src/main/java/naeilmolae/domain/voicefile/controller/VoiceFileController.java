package naeilmolae.domain.voicefile.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.voicefile.domain.ProvidedFile;
import naeilmolae.domain.voicefile.domain.VoiceFile;
import naeilmolae.domain.voicefile.dto.request.UploadContentRequestDto;
import naeilmolae.domain.voicefile.dto.response.AvailableVoiceFileResponseDto;
import naeilmolae.domain.voicefile.dto.response.VoiceFileMetaResponseDto;
import naeilmolae.domain.voicefile.service.ProvidedFileService;
import naeilmolae.domain.voicefile.service.VoiceFileService;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import naeilmolae.global.util.S3FileComponent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voicefiles")
public class VoiceFileController {
    private final VoiceFileService voiceFileService;
    private final S3FileComponent s3FileComponent;
    private final ProvidedFileService providedFileService;


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

    @Operation(summary = "[VALID] [봉사자] 녹음 4단계: 음성 파일 업로드", description = "사용자가 녹음한 음성 파일을 업로드합니다. 분석 결과를 받기 위해 업로드합니다. 업로드시 바로 분석이 진행됩니다.")
    @PostMapping(value = "/{voiceFileId}", consumes = {"multipart/form-data"})
    public BaseResponse<String> uploadVoiceFile(@CurrentMember Member member,
                                                @PathVariable Long voiceFileId,
                                                @RequestParam("file") MultipartFile multipartFile) {

        voiceFileService.verifyUserFile(member.getId(), voiceFileId);
        // category를 "voice"로 지정하여 파일을 S3에 업로드
        String fileUrl = s3FileComponent.uploadFile("voice", multipartFile);
        voiceFileService.saveVoiceFileUrl(voiceFileId, fileUrl);

        // 성공 시, 파일의 S3 URL을 응답으로 반환
        return BaseResponse.onSuccess(fileUrl); // TODO 응답
    }

    @Operation(summary = "[VALID] [봉사자] 녹음 5단계: 분석 결과 조회", description = "제공한 파일의 분석 결과를 조회합니다. N초마다 polling하여 분석 결과를 받아옵니다.")
    @PostMapping("/analysis/{voiceFileId}")
    public BaseResponse<String> analysisVoiceFile(@CurrentMember Member member,
                                                  @PathVariable Long voiceFileId) {
        voiceFileService.verifyUserFile(member.getId(), voiceFileId);
        voiceFileService.getAnalysisResultRepository(voiceFileId);
        return BaseResponse.onSuccess("OK");
    }

    @Operation(summary = "[VALID] [청년] 청취 1단계: 사용 가능한 음성 파일 ID 조회", description = "사용자가 청취할 수 있는 음성 파일 ID를 조회합니다.")
    @GetMapping
    public BaseResponse<AvailableVoiceFileResponseDto> getAvailableDataList(@CurrentMember Member member,
                                                                            @RequestParam("alarm-id") Long alarmId) { // 실제는 childrenCategoryId 임
        VoiceFile voiceFile = voiceFileService.getAvailableDataList(member.getId(), alarmId);
        ProvidedFile save = providedFileService.save(member.getId(), voiceFile.getId());// TODO 나중에 분석 결과 저장하는 API로 변경
        return BaseResponse.onSuccess(AvailableVoiceFileResponseDto.from(voiceFile, save.getId()));
    }
}
