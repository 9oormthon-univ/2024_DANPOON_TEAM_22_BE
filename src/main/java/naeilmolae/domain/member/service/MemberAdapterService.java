package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberAdapterService {

    private final MemberService memberService;

    public List<Member> getAllYouthMember() {
        return memberService.getAllYouthMember();
    }

    public Member findById(Long id) {
        return memberService.findById(id);
    }

    public List<Member> getAllHelperMember() {
        return memberService.getAllHelperMember();
    }

    public LocalDateTime getLastLoginDate(Member member) {
        return memberService.getLastLoginDate(member);
    }

}
