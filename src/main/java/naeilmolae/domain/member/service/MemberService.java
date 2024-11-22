package naeilmolae.domain.member.service;

import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberInfoResponseDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberService {

    Member findById(Long id) throws UsernameNotFoundException;

    // 회원 저장
    Member saveEntity(Member member);
    // 회원가입
    MemberIdResponseDto signUp(Member member, MemberInfoRequestDto request);
    // 회원 탈퇴
    MemberIdResponseDto withdrawal(Member member);
    // 회원 정보 수정
    MemberIdResponseDto updateMemberInfo(Member member, MemberInfoRequestDto request);
    // 회원 정보 조회
    MemberInfoResponseDto getMemberInfo(Member member);
}