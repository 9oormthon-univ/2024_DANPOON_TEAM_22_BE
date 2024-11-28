package naeilmolae.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import naeilmolae.domain.member.domain.Gender;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "회원 정보 응답 객체")
public class MemberInfoResponseDto {

    @Schema(description = "회원 이름", example = "John Doe")
    private String name;

    @Schema(description = "회원 성별", example = "MALE, FEMALE 두가지로 반환")
    private Gender gender;

    @Schema(description = "회원 프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "회원 역할 (청년 또는 조력자)", example = "YOUTH,  HELPER 두가지 중 하나로 반환")
    private Role role;

    @Schema(description = "회원 생년 월일", example = "1995-01-01T00:00:00.000Z, 신경 안써도 되는 필드임")
    private LocalDateTime birth;

    @Schema(description = "청년회원 정보")
    private YouthMemberInfoDto youthMemberInfoDto;
}
