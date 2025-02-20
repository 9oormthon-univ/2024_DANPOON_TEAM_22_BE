package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.response.SimpleMemberDto;
import naeilmolae.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberAdapterService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

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

    public Map<Long, SimpleMemberDto> getSimpleMemberDtoMapByIds(List<Long> memberIds) {
        List<Member> members = memberRepository.findByIdIn(memberIds);
        return members.stream()
                .map(SimpleMemberDto::from)
                .collect(Collectors.toMap(SimpleMemberDto::getId, Function.identity()));
    }

}
