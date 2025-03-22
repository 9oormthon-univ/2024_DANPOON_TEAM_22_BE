package naeilmolae.domain.voicefile.dto.response;

public record AnalysisResponseDto(Long voiceFileId, String analysisResultStatus, String sttContent) {
}