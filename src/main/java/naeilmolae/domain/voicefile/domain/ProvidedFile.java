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

    private String thanksMessage; // TODO 나중에 varchar 255를 늘려야할 수도

    private boolean isConsumerSaved = false;

    public ProvidedFile(VoiceFile voiceFile, Long consumerId) {
//        if(consumer.getRole() == Role.HELPER) {
//            throw new IllegalArgumentException("파일을 제공할 수 없는 사용자입니다.");
//        }
        this.voiceFile = voiceFile;
        this.consumerId = consumerId;
    }

    public boolean addThanksMessage(String message) {
        // TODO : 감사 메시지 중복 체크
//        if (thanksMessage != null) {
//            throw new IllegalStateException("이미 감사 메시지가 존재합니다.");
//        }

        this.thanksMessage = message;


        return true;
    }

    public boolean setConsumerSaved() {
        this.isConsumerSaved = true;

        return true;
    }

}
