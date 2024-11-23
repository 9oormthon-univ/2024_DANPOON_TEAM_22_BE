package naeilmolae.domain.voicefile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.domain.alarm.domain.Alarm;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.global.common.base.BaseEntity;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;

import static naeilmolae.domain.voicefile.domain.VoiceFileStatus.AUDIO_SUBMITTED;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoiceFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 대용량 텍스트
    @Column(unique = true)
    private String fileUrl; // 저장된 음성 url

    @Enumerated(EnumType.STRING)
    private VoiceFileStatus status = VoiceFileStatus.TEXT_SUBMITTED;

    @OneToOne(mappedBy = "voiceFile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "analysis_result_id")
    private AnalysisResult analysisResult;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    // TODO 음성 파형 넣어야함

    public VoiceFile(Member member, Alarm alarm, String content) {
        //  Alarm 서비스가 구현 되어야 한다.
        if (alarm.getAlarmCategory().isRoot()) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST); // TODO 예외 처리
        }
        this.member = member;
        this.content = content;
        this.alarm = alarm; // TODO Alarm 서비스가 구현 되어야 한다.
    }

    /**
     * 사용자가 읽을 텍스트 파일 변경
     *
     * @param content 변경할 텍스트
     */
    public void changeContent(String content) {
        this.content = content;
    }

    /**
     * 음성 파일 저장하기, 이후 분석 진행
     *
     * @param fileUrl 저장된 음성 파일 ID
     */
    // TODO refactor
    public void saveFileUrl(String fileUrl) {
        if (this.content == null) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST); // TODO 예외 처리
        }

        this.fileUrl = fileUrl;
        this.status = AUDIO_SUBMITTED;
    }


    // TODO 상태 관리 어떻게 할지 고민..
    public void prepareAnalysis() {
        if (this.status != AUDIO_SUBMITTED) {
            throw new IllegalStateException("분석 요청은 녹음 완료 상태에서만 가능합니다."); // TODO 예외 처ㅣㄹ
        }
        if (this.getFileUrl() == null) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }
    }

    public AnalysisResult saveResult(AnalysisResultStatus analysisResultStatus, String sttContent) {
        // 결과 저장
        AnalysisResult analysisResult = new AnalysisResult(this,
                analysisResultStatus,
                sttContent);
        this.analysisResult = analysisResult;

        return analysisResult;
    }
}
