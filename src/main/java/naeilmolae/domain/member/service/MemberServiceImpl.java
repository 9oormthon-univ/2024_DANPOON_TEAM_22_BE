package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberInfoResponseDto;
import naeilmolae.domain.member.dto.response.MemberNumResponseDto;
import naeilmolae.domain.member.mapper.MemberMapper;
import naeilmolae.domain.member.repository.YouthMemberInfoRepository;
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
    private final YouthMemberInfoRepository youthMemberInfoRepository;

    private final MemberRefreshTokenService refreshTokenService;

    @Override
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

    //회원 가입
    @Transactional
    public MemberIdResponseDto signUp(Member member, MemberInfoRequestDto request) {
        Member loginMember = findById(member.getId());

        // 기본 정보 업데이트
        updateMemberBasicInfo(loginMember, request);

        // 역할에 따라 추가 정보 처리
        handleRoleSpecificInfo(loginMember, request);

        return new MemberIdResponseDto(saveEntity(loginMember).getId());
    }

    //회원 정보 수정
    @Override
    @Transactional
    public MemberIdResponseDto updateMemberInfo(Member member, MemberInfoRequestDto request) {
        Member loginMember = findById(member.getId());

        // 기본 정보 업데이트
        updateMemberBasicInfo(loginMember, request);

        // 역할에 따라 추가 정보 처리
        handleRoleSpecificInfo(loginMember, request);

        return new MemberIdResponseDto(saveEntity(loginMember).getId());
    }

    // 기본 정보 업데이트
    private void updateMemberBasicInfo(Member member, MemberInfoRequestDto request) {
        member.updateMemberInfo(request);
    }

    // 역할에 따라 추가 정보 처리
    private void handleRoleSpecificInfo(Member member, MemberInfoRequestDto request) {
        if (request.role().equals(Role.YOUTH)) {
            // 청년 정보 처리
            YouthMemberInfo youthMemberInfo = member.getYouthMemberInfo();
            if (youthMemberInfo == null) {
                // 청년 정보가 없으면 새로 저장
                youthMemberInfo = MemberMapper.toYouthMemberInfo(request.youthMemberInfoDto());
                member.setYouthMemberInfo(youthMemberInfo);
                youthMemberInfoRepository.save(youthMemberInfo);
            } else {
                // 청년 정보가 있으면 업데이트
                youthMemberInfo.update(request.youthMemberInfoDto());
            }
        }
    }

    @Override
    public MemberInfoResponseDto getMemberInfo(Member member) {
        Member loginMember = findById(member.getId());
        // 청년인 경우 청년 정보도 함께 반환
        if (loginMember.getRole().equals(Role.YOUTH)) {
            return MemberMapper.toMemberInfoResponseDto(
                    loginMember,
                    MemberMapper.toYouthMemberInfoDto(loginMember.getYouthMemberInfo())
            );
        }
        // 청년이 아닌 경우(조력자)
        return MemberMapper.toMemberInfoResponseDto(loginMember);
    }

    @Override
    public MemberNumResponseDto getMemberNum(Role role) {
        return new MemberNumResponseDto(memberRepository.countAllByRole(role));
    }
}
