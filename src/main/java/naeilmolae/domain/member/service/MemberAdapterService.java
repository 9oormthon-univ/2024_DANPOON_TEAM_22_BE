package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberAdapterService {

    private final MemberService memberService;

    public List<Member> getAllYouthMember() {
        return memberService.getAllYouthMember();
    }
}
