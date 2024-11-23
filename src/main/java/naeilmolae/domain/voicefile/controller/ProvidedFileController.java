package naeilmolae.domain.voicefile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.voicefile.domain.ProvidedFile;
import naeilmolae.domain.voicefile.dto.request.ThanksMessageRequestDto;
import naeilmolae.domain.voicefile.dto.response.ProvidedFileResponseDto;
import naeilmolae.domain.voicefile.dto.response.VoiceFileReactionSummaryResponseDto;
import naeilmolae.domain.voicefile.service.ProvidedFileService;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/providedfile")
public class ProvidedFileController {
    private final ProvidedFileService providedFileService;

    @Operation(summary = "[봉사자] 동기부여 2단계: 자신의 녹음에 대해 사람들이 남긴 정보 요약 API", description = "동기부여 메인 페이지 내용, 자신의 녹음에 대해 사람들이 남긴 정보 요약")
    @GetMapping("/summary")
    public BaseResponse<VoiceFileReactionSummaryResponseDto> getTotalReaction(@CurrentMember Member member) {
        return BaseResponse.onSuccess(providedFileService.getTotalReaction(member.getId()));
    }

    @Operation(summary = "[VALID] [봉사자] 동기부여 3단계: 청년의 편지 조회", description = "동기부여0 페이지 내용, 청년의 편지를 조회합니다.")
    @GetMapping("/list")
    public BaseResponse<Page<ProvidedFileResponseDto>> getProvidedFileList(@CurrentMember Member member,
                                                                           @RequestParam(value = "parent_category_id", required = false) Optional<Long> parentCategoryId,
                                                                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProvidedFile> providedFiles =
                providedFileService.getProvidedFiles(member.getId(), parentCategoryId, pageable);

        return BaseResponse.onSuccess(providedFiles.map(ProvidedFileResponseDto::from));
    }

    @Operation(summary = "[VALID] [청년] 청취 3단계: 감사 메시지 보내기", description = "청년이 봉사자에게 감사 메시지를 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "저장 성공"),
    })
    @PostMapping("/{providedFileId}/comment")
    public BaseResponse<Boolean> likeProvidedFile(@CurrentMember Member member,
                                               @PathVariable Long providedFileId,
                                               @RequestBody ThanksMessageRequestDto requestDto) {
        providedFileService.likeProvidedFile(member.getId(), providedFileId, requestDto.message());

        return BaseResponse.onSuccess(providedFileService.likeProvidedFile(member.getId(),
                providedFileId,
                requestDto.message()));
    }

    @Operation(summary = "[VALID] [청년] 청취 4단계: 북마크", description = "청년이 봉사자의 음성을 북마크합니다.")
    @PostMapping("/{providedFileId}/bookmark")
    public BaseResponse<Boolean> bookmarkProvidedFile(@CurrentMember Member member,
                                                      @PathVariable Long providedFileId) {
        providedFileService.bookmarkProvidedFile(member.getId(), providedFileId);
        return BaseResponse.onSuccess(providedFileService.bookmarkProvidedFile(member.getId(), providedFileId));
    }


}
