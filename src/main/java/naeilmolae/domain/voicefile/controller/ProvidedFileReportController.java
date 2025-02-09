package naeilmolae.domain.voicefile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.voicefile.dto.request.ProvidedFileReportRequestDto;
import naeilmolae.domain.voicefile.service.ProvidedFileReportService;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProvidedFileReportController {
    private final ProvidedFileReportService providedFileReportService;


    @Operation(summary = "감사 메시지 신고", description = "감사 메시지에 부적절한 내용이 있을 경우 신고합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "저장 성공"),
    })
    @PostMapping("/{providedFileId}/report")
    public BaseResponse<Boolean> likeProvidedFile(@CurrentMember Member member,
                                                  @PathVariable Long providedFileId,
                                                  @RequestBody ProvidedFileReportRequestDto requestDto) {
        providedFileReportService.reportProvidedFile(member.getId(), providedFileId, requestDto.reason());

        return BaseResponse.onSuccess(true);
    }
}
