package naeilmolae.domain.voicefile.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.global.common.base.BaseEntity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(length = 1000) // 최대 5개의 메시지를 저장하기 위해 길이를 늘림
    private String thanksMessages; // 세미콜론(;)으로 구분하여 저장

    private boolean isConsumerSaved = false;

    public ProvidedFile(VoiceFile voiceFile, Long consumerId) {
        this.voiceFile = voiceFile;
        this.consumerId = consumerId;
        this.thanksMessages = "";
    }

    /**
     * 감사 메시지를 추가 (중복 방지 및 최대 5개 제한)
     * @param message 추가할 메시지
     * @return 추가 성공 여부 (true: 추가됨, false: 추가 안됨)
     */
    public boolean addThanksMessage(String message) {
        Set<String> messages = getThanksMessagesSet();
        if (messages.size() >= 5 || messages.contains(message)) {
            return false; // 5개 이상이거나 중복이면 추가 안함
        }
        messages.add(message);
        this.thanksMessages = String.join(";", messages); // 다시 문자열로 변환하여 저장
        return true;
    }

    /**
     * 감사 메시지 삭제
     * @param message 삭제할 메시지
     * @return 삭제 성공 여부 (true: 삭제됨, false: 해당 메시지가 없음)
     */
    public boolean removeThanksMessage(String message) {
        Set<String> messages = getThanksMessagesSet();
        if (!messages.remove(message)) {
            return false; // 메시지가 없으면 삭제 실패
        }
        this.thanksMessages = String.join(";", messages); // 다시 문자열로 변환하여 저장
        return true;
    }

    public boolean setConsumerSaved() {
        this.isConsumerSaved = true;
        return true;
    }

    /**
     * 현재 저장된 감사 메시지를 중복 없이 Set 형태로 반환
     * @return 중복 없는 감사 메시지 Set
     */
    public Set<String> getThanksMessagesSet() {
        if (thanksMessages == null || thanksMessages.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return new LinkedHashSet<>(List.of(thanksMessages.split(";")));
    }
}
