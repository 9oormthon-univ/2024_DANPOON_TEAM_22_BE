package naeilmolae.domain.voicefile.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.global.common.base.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class ProvidedFileReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고된 ProvidedFile과 다대일 관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provided_file_id", nullable = false)
    private ProvidedFile providedFile;

    // 신고한 사용자의 ID (예: 신고자)
    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 신고 사유 (길이 제한 등 필요하면 조정)
    @Column(nullable = false, length = 500)
    private String reason;

    public ProvidedFileReport(ProvidedFile providedFile, Member member, String reason) {
        this.providedFile = providedFile;
        this.member = member;
        this.reason = reason;
    }
}
