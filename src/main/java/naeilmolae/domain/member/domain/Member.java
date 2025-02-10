package naeilmolae.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.global.common.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Setter
    private String name;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String clientId;

    private LocalDateTime birth;

    private String deviceId;

    private String fcmToken;

    // 편의상 DB에 저장, 실제로는 저장하지 않게 해야 함
    @Setter
    private String refreshToken;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "youth_member_info_id") // 외래 키 설정
    private YouthMemberInfo youthMemberInfo;

    @Builder
    public Member(
            String name, Gender gender, String profileImage
            , Role role, LoginType loginType, String clientId
            , LocalDateTime birth, String deviceId) {
        this.name = name;
        this.gender = gender;
        this.profileImage = profileImage;
        this.role = role;
        this.loginType = loginType;
        this.clientId = clientId;
        this.birth = birth;
        this.deviceId = deviceId;
    }

    // 널이나 빈 값이 아닌 경우에만 수정
    public void updateMemberInfo(MemberInfoRequestDto request) {
        Optional.ofNullable(request.name()).filter(name -> !name.isEmpty()).ifPresent(name -> this.name = name);
        Optional.ofNullable(request.profileImage()).filter(image -> !image.isEmpty()).ifPresent(image -> this.profileImage = image);
        Optional.ofNullable(request.gender()).ifPresent(gender -> this.gender = gender);
        Optional.ofNullable(request.role()).ifPresent(role -> this.role = role);
        Optional.ofNullable(request.birth()).ifPresent(birth -> this.birth = birth);
        Optional.ofNullable(request.fcmToken()).filter(token -> !token.isEmpty()).ifPresent(token -> this.fcmToken = token);
    }

    public boolean changeRole(Role role) {
        this.role = role;
        return true;
    }
}
