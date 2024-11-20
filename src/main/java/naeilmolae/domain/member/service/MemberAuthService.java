package naeilmolae.domain.member.service;

import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.MemberGenerateTokenResponseDto;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;

public interface MemberAuthService {
    // 소셜 로그인
    MemberLoginResponseDto socialLogin(final String accessToken, LoginType loginType);
    // 새로운 액세스 토큰 발급
    MemberGenerateTokenResponseDto generateNewAccessToken(String refreshToken, Member member);
    // 로그아웃
    MemberIdResponseDto logout(Member member);

}

