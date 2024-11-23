package naeilmolae.domain.voicefile.evnets;

public interface VoiceFileEventListener {
    void handleEvent(VoiceFileAnalysisEvent event);
}
