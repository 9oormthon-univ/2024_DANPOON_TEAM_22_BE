package naeilmolae.domain.member.dto.request;

import naeilmolae.domain.member.domain.Gender;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;

import java.time.LocalDateTime;

//@ValidRoleBasedRequest // 선택한 역할과 다른 정보가 입력되었는지 검증하는 어노테이션
public record MemberInfoRequestDto(
        String name,
        Gender gender,
        String profileImage,
        Role role,
        LocalDateTime birth,
        YouthMemberInfoDto youthMemberInfoDto){
}
