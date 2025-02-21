package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.*;
import naeilmolae.domain.member.dto.request.YouthMemberInfoUpdateDto;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.request.WithdrawalReasonRequest;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberInfoResponseDto;
import naeilmolae.domain.member.dto.response.MemberNumResponseDto;
import naeilmolae.domain.member.dto.response.YouthMemberInfoResponseDto;
import naeilmolae.domain.member.mapper.MemberMapper;
import naeilmolae.domain.member.repository.HelperMemberInfoRepository;
import naeilmolae.domain.member.repository.MemberRepository;
import naeilmolae.domain.member.repository.MemberWithdrawalReasonRepository;
import naeilmolae.domain.member.repository.YouthMemberInfoRepository;
import naeilmolae.domain.member.status.MemberErrorStatus;
import naeilmolae.domain.weather.domain.Grid;
import naeilmolae.domain.weather.service.GridService;
import naeilmolae.global.common.exception.RestApiException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final YouthMemberInfoRepository youthMemberInfoRepository;
    private final MemberWithdrawalReasonRepository memberWithdrawalReasonRepository;

    private final MemberRefreshTokenService refreshTokenService;
    private final GridService gridService;

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

    // 회원가입 함수 (멤버 기본 정보 등록)
    @Override
    @Transactional
    public MemberIdResponseDto signUpInfo(Member member, MemberInfoRequestDto request) {

        if (member.getRole().equals(Role.HELPER)) {
            //나이가 성인이 아니면 예외처리 (만 19세가 아닌 성인이 기준)
            // 현재 연도 - 태어난 연도 < 19 이면 예외처리
            if (LocalDateTime.now().getYear() - request.birth().getYear() < 19) {
                throw new RestApiException(MemberErrorStatus.INVALID_HELPER_AGES);
            }
        }

        // 기본 정보 업데이트
        updateMemberBasicInfo(member, request);

        return new MemberIdResponseDto(saveEntity(member).getId());
    }

    // 회원가입 함수 (청년 위치 정보 등록)
    @Override
    @Transactional
    public MemberIdResponseDto signUpYouth(Member member, YouthMemberInfoDto request) {

        // 기본 정보 업데이트
        handleRoleSpecificInfo(member, request);

        return new MemberIdResponseDto(saveEntity(member).getId());
    }

    // 회원 탈퇴 함수
    @Override
    @Transactional
    public MemberIdResponseDto withdrawal(Member member, WithdrawalReasonRequest request) {
        // 멤버 soft delete
        Member loginMember = findById(member.getId());

        // refreshToken 삭제
        refreshTokenService.deleteRefreshToken(loginMember);

        // 멤버 soft delete
        loginMember.delete();

        // 탈퇴 사유 저장
        request.reasonList()
                .forEach(reason -> memberWithdrawalReasonRepository.save(new MemberWithdrawalReason(reason)));

        return new MemberIdResponseDto(loginMember.getId());
    }

    //회원 정보 수정
    @Override
    @Transactional
    public MemberIdResponseDto updateMemberInfo(Member member, MemberInfoRequestDto request) {
        Member loginMember = findById(member.getId());

        // 기본 정보 업데이트
        updateMemberBasicInfo(loginMember, request);

        return new MemberIdResponseDto(saveEntity(loginMember).getId());
    }

    //청년 회원 정보 수정
    @Override
    @Transactional
    public MemberIdResponseDto updateYouthMemberInfo(Member member, YouthMemberInfoUpdateDto request) {
        Member loginMember = findById(member.getId());
        YouthMemberInfo youthMemberInfo = loginMember.getYouthMemberInfo();

        if (youthMemberInfo == null) {
            throw new RestApiException(MemberErrorStatus.NOT_YOUTH);
        }

        // 청년 정보 업데이트
        youthMemberInfo.setBreakfast(request.getBreakfast());
        youthMemberInfo.setLunch(request.getLunch());
        youthMemberInfo.setDinner(request.getDinner());
        youthMemberInfo.setWakeUpTime(request.getWakeUpTime());
        youthMemberInfo.setSleepTime(request.getSleepTime());
        youthMemberInfo.setOutgoingTime(request.getOutgoingTime());

        youthMemberInfo.setBreakfastAlarm(request.isBreakfastAlarm());
        youthMemberInfo.setLunchAlarm(request.isLunchAlarm());
        youthMemberInfo.setDinnerAlarm(request.isDinnerAlarm());
        youthMemberInfo.setWakeUpAlarm(request.isWakeUpAlarm());
        youthMemberInfo.setSleepAlarm(request.isSleepAlarm());
        youthMemberInfo.setOutgoingAlarm(request.isOutgoingAlarm());


        return new MemberIdResponseDto(saveEntity(loginMember).getId());
    }

    @Override
    public YouthMemberInfoResponseDto getYouthMemberInfo(Member member) {
        Member loginMember = findById(member.getId());
        YouthMemberInfo youthMemberInfo = loginMember.getYouthMemberInfo();

        if (youthMemberInfo == null) {
            throw new RestApiException(MemberErrorStatus.NOT_YOUTH);
        }
        return YouthMemberInfoResponseDto.from(youthMemberInfo);
    }

    @Override
    public HelperMemberInfo getHelperMemberInfo(Member member) {
        Member findMember = memberRepository.findByIdAndHasHelperInfo(member.getId())
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.NOT_HELPER));

        return findMember.getHelperMemberInfo();
    }

    // 기본 정보 업데이트
    private void updateMemberBasicInfo(Member member, MemberInfoRequestDto request) {
        member.updateMemberInfo(request);
    }

    // 역할에 따라 추가 정보 처리
    @Transactional
    public void handleRoleSpecificInfo(Member member, YouthMemberInfoDto request) {
        if (member.getRole().equals(Role.YOUTH)) {
            // 청년 정보 처리
            YouthMemberInfo youthMemberInfo = member.getYouthMemberInfo();
            if (youthMemberInfo == null) {
                // 청년 정보가 없으면 새로 저장
                youthMemberInfo = MemberMapper.toYouthMemberInfo(request);

                // 위치 X, Y 좌표 저장
                Grid grid = gridService.getGridCoordinates(youthMemberInfo.getLatitude(), youthMemberInfo.getLongitude());
                youthMemberInfo.setGridId(grid.getId());

                member.setYouthMemberInfo(youthMemberInfo);
                youthMemberInfoRepository.save(youthMemberInfo);
            } else {
                // 청년 정보가 있으면 업데이트
                youthMemberInfo.updateYouthMemberInfoDto(request);
            }
        }
    }

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

    @Override
    public List<Member> getAllYouthMember() {
        return memberRepository.findAllYouthMembersWithInfo(Role.YOUTH);
    }

    @Override
    public List<Member> getAllHelperMember() {
        return memberRepository.findAllHelperMembersWithInfo(Role.HELPER);
    }

    @Override
    public LocalDateTime getLastLoginDate(Member member) {
        return refreshTokenService.getLastIssueAt(member.getRefreshToken());
    }
}
