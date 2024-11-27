package naeilmolae.domain.member.service;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAdapterService {

    private final MemberService memberService;

//    public MemberIdResponseDto findById(Long memberId) {
//
//    }
}
