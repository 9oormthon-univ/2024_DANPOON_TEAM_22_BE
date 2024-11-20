package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.client.KakaoMemberClient;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.MemberGenerateTokenResponseDto;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.mapper.MemberMapper;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AuthErrorStatus;
import naeilmolae.global.config.security.jwt.JwtProvider;
import naeilmolae.global.config.security.jwt.TokenInfo;
import naeilmolae.global.config.security.jwt.TokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAuthServiceImpl implements MemberAuthService {

    public final MemberRepository memberRepository;

    public final MemberService memberService;
    public final MemberRefreshTokenService refreshTokenService;

    public final KakaoMemberClient kakaoMemberClient;

    public final JwtProvider jwtTokenProvider;

    public final MemberMapper memberMapper;

    // 소셜 로그인을 수행하는 함수
    @Override
    public MemberLoginResponseDto socialLogin(String accessToken, LoginType loginType){
        // 로그인 구분
        if(loginType.equals(LoginType.KAKAO))
            return loginByKakao(accessToken);

        return null;
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

    private MemberLoginResponseDto loginByKakao(final String accessToken){
        // kakao 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = kakaoMemberClient.getkakaoClientID(accessToken);
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndLoginType(clientId, LoginType.KAKAO);

        // 1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()) {
            return saveNewMember(clientId, LoginType.KAKAO);
        }
        // 2. 있으면 : 새로운 토큰 반환
        boolean isServiceMember = getMember.get().getName() != null;
        return getNewToken(getMember.get(), isServiceMember);
    }

    private MemberLoginResponseDto saveNewMember(String clientId, LoginType loginType) {
        Member member = MemberMapper.toMember(clientId, loginType);
        Member newMember =  memberService.saveEntity(member);

        return getNewToken(newMember, false);
    }

    private MemberLoginResponseDto getNewToken(Member member, boolean isServiceMember) {
        // jwt 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId().toString(), member.getRole().toString());
        // refreshToken 디비에 저장
        refreshTokenService.saveRefreshToken(tokenInfo.refreshToken(), member);

        return MemberMapper.toLoginMember(member, tokenInfo, isServiceMember);
    }
}