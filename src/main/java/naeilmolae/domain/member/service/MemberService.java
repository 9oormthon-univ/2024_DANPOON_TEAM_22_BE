package naeilmolae.domain.member.service;

import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberService {

    Member findById(Long id) throws UsernameNotFoundException;

    // 회원 저장
    Member saveEntity(Member member);
    // 회원 탈퇴
    MemberIdResponseDto withdrawal(Member member);
}
