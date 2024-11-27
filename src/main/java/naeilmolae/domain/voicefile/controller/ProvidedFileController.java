package naeilmolae.domain.voicefile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.dto.response.AlarmResponseDto;
import naeilmolae.domain.alarm.service.AlarmAdapterService;
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

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/providedfile")
public class ProvidedFileController {
    private final ProvidedFileService providedFileService;
    private final AlarmAdapterService alarmAdapterService;

    @Operation(summary = "[봉사자] 동기부여 2단계: 자신의 녹음에 대해 사람들이 남긴 정보 요약 API", description = "동기부여 메인 페이지 내용, 자신의 녹음에 대해 사람들이 남긴 정보 요약")
    @GetMapping("/summary")
    public BaseResponse<VoiceFileReactionSummaryResponseDto> getTotalReaction(@CurrentMember Member member) {
        return BaseResponse.onSuccess(providedFileService.getTotalReaction(member.getId()));
    }

    @Operation(summary = "[VALID] [봉사자] 동기부여 3단계: 청년의 편지 조회", description = "동기부여0 페이지 내용, 청년의 편지를 조회합니다.")
    @GetMapping("/list")
    public BaseResponse<Page<ProvidedFileResponseDto>> getProvidedFileList(@CurrentMember Member member,
                                                                           @RequestParam(value = "parentCategory", required = false) String parentCategory,
                                                                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // TODO 아래를 새로운 서비스로 만들어야 한다.
        Page<ProvidedFile> providedFiles =
                providedFileService.getProvidedFiles(member.getId(), parentCategory, pageable);

        // Step 1: ProvidedFile의 voiceFile의 alarmId를 추출
        Set<Long> alarmIds = providedFiles.stream()
                .map(providedFile -> providedFile.getVoiceFile().getAlarmId())
                .collect(Collectors.toSet());

        // Step 2: AlarmId를 사용하여 AlarmResponseDto Map 생성
        Map<Long, AlarmResponseDto> alarms = alarmAdapterService.findAlarmsByIds(alarmIds);

        // Step 3: Page의 T를 ProvidedFileResponseDto로 변환
        Page<ProvidedFileResponseDto> responseDtos = providedFiles.map(providedFile -> {
            AlarmResponseDto alarm = alarms.get(providedFile.getVoiceFile().getAlarmId());
            return ProvidedFileResponseDto.from(providedFile, alarm.getAlarmCategory());
        });

        return BaseResponse.onSuccess(responseDtos);
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
