package naeilmolae.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "소셜 로그인 성공 응답 객체")
public class MemberLoginResponseDto {

    @Schema(description = "회원 고유 ID", example = "12345")
    private Long memberId;

    @Schema(description = "로그인 시 발급되는 JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String accessToken;

    @Schema(description = "로그인 시 발급되는 JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String refreshToken;

    @Schema(description = "서비스 회원 여부 (true: 서비스에 등록된 회원, false: 비회원, 비회원시 회원가입 진행)", example = "true")
    private boolean isServiceMember;
}

