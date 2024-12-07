package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.MemberGenerateTokenResponseDto;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.strategy.context.LoginContext;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AuthErrorStatus;
import naeilmolae.global.config.security.jwt.JwtProvider;
import naeilmolae.global.config.security.jwt.TokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAuthServiceImpl implements MemberAuthService {

//    public final MemberRepository memberRepository;

    public final MemberService memberService;
    public final MemberRefreshTokenService refreshTokenService;

//    public final KakaoMemberClient kakaoMemberClient;

    public final JwtProvider jwtTokenProvider;

    private final LoginContext loginContext;

    // 소셜 로그인을 수행하는 함수
    @Override
    @Transactional
    public MemberLoginResponseDto socialLogin(String accessToken, LoginType loginType) {
        return loginContext.executeStrategy(accessToken, loginType);
    }

    // 새로운 액세스 토큰 발급 함수
    @Override
    @Transactional
    public MemberGenerateTokenResponseDto generateNewAccessToken(String refreshToken, Member member) {

        Member loginMember = memberService.findById(member.getId());

        // 만료된 refreshToken인지 확인
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw new RestApiException(AuthErrorStatus.EXPIRED_REFRESH_TOKEN);

        //편의상 refreshToken을 DB에 저장 후 비교하는 방식으로 감 (비추천)
        String savedRefreshToken = loginMember.getRefreshToken();

        // 디비에 저장된 refreshToken과 동일하지 않다면 유효하지 않음
        if (!refreshToken.equals(savedRefreshToken))
            throw new RestApiException(AuthErrorStatus.INVALID_REFRESH_TOKEN);

        return new MemberGenerateTokenResponseDto(
                jwtTokenProvider.generateToken(
                        loginMember.getId().toString(), member.getRole().toString(), TokenType.ACCESS)
        );
    }

    // 로그아웃 함수
    @Override
    @Transactional
    public MemberIdResponseDto logout(Member member) {
        Member loginMember = memberService.findById(member.getId());

        refreshTokenService.deleteRefreshToken(loginMember);
        return new MemberIdResponseDto(loginMember.getId());
    }

}