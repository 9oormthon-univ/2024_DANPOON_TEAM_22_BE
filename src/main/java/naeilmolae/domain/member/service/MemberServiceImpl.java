package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.status.MemberErrorStatus;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.global.common.exception.RestApiException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    private final MemberRefreshTokenService refreshTokenService;

    public Member findById(Long id) throws UsernameNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.EMPTY_MEMBER));
    }

    // 회원 저장
    @Override
    @Transactional
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    // 회원 탈퇴 함수
    @Override
    @Transactional
    public MemberIdResponseDto withdrawal(Member member) {
        // 멤버 soft delete
        Member loginMember = findById(member.getId());

        // refreshToken 삭제
        refreshTokenService.deleteRefreshToken(loginMember);

        // 멤버 soft delete
        loginMember.delete();

        return new MemberIdResponseDto(loginMember.getId());
    }
}
