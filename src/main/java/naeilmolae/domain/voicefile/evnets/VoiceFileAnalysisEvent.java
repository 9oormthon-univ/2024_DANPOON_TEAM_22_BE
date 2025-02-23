package naeilmolae.domain.voicefile.evnets;

import lombok.ToString;

public record VoiceFileAnalysisEvent(Long voiceFileId, String fileUrl, String content) {
}
