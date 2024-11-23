package naeilmolae.domain.voicefile.evnets;

public record VoiceFileAnalysisEvent(Long voiceFileId, String fileUrl, String content) {
}
