package naeilmolae.domain.voicefile.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class ProvidedFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_file_id")
    private VoiceFile voiceFile;

    private Long consumerId;

    private String thanksMessage;

    private boolean isConsumerSaved = false;

    public ProvidedFile(VoiceFile voiceFile, Long consumerId) {
        this.voiceFile = voiceFile;
        this.consumerId = consumerId;
    }

    public boolean addThanksMessage(String message) {
        this.thanksMessage = message;
        return true;
    }

    public boolean setConsumerSaved() {
        this.isConsumerSaved = true;

        return true;
    }

}
