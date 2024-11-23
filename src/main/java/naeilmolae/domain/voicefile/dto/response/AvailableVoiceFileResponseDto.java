package naeilmolae.domain.voicefile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naeilmolae.domain.voicefile.domain.VoiceFile;

@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "사용 가능한 음성 파일 응답 객체")
public class AvailableVoiceFileResponseDto {

    @Schema(description = "음성 파일 고유 ID (Long)", example = "1")
    private Long voiceFileId;

    @Schema(description = "음성 파일 URL", example = "https://example.com/voice.mp3")
    private String fileUrl;

    private Long providedFileId;

    public static AvailableVoiceFileResponseDto from(VoiceFile voiceFile, Long providedFileId) {
        AvailableVoiceFileResponseDto availableVoiceFileResponseDto = new AvailableVoiceFileResponseDto();
        availableVoiceFileResponseDto.setVoiceFileId(voiceFile.getId());
        availableVoiceFileResponseDto.setFileUrl(voiceFile.getFileUrl());
        availableVoiceFileResponseDto.setProvidedFileId(providedFileId);

        return availableVoiceFileResponseDto;
    }
}
