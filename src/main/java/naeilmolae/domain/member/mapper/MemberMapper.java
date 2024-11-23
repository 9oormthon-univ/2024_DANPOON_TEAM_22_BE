package naeilmolae.domain.member.mapper;

import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.domain.YouthMemberInfo;
import naeilmolae.domain.member.dto.YouthMemberInfoDto;
import naeilmolae.domain.member.dto.response.MemberInfoResponseDto;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.global.config.security.jwt.TokenInfo;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public static Member toMember(final String clientId, LoginType loginType){
        return Member.builder()
                .clientId(clientId)
                .loginType(loginType)
                .build();
    }

    public static MemberLoginResponseDto toLoginMember(final Member member, TokenInfo tokenInfo, boolean isServiceMember, Role role) {
        return MemberLoginResponseDto.builder()
                .memberId(member.getId())
                .accessToken(tokenInfo.accessToken())
                .refreshToken(tokenInfo.refreshToken())
                .isServiceMember(isServiceMember)
                .role(role)
                .build();
    }

    public static YouthMemberInfo toYouthMemberInfo(YouthMemberInfoDto dto) {
        return YouthMemberInfo.builder()
                .wakeUpTime(dto.getWakeUpTime())
                .sleepTime(dto.getSleepTime())
                .breakfast(dto.getBreakfast())
                .lunch(dto.getLunch())
                .dinner(dto.getDinner())
                .build();
    }

    public static YouthMemberInfoDto toYouthMemberInfoDto(YouthMemberInfo youthMemberInfo) {
        return YouthMemberInfoDto.builder()
                .wakeUpTime(youthMemberInfo.getWakeUpTime())
                .sleepTime(youthMemberInfo.getSleepTime())
                .breakfast(youthMemberInfo.getBreakfast())
                .lunch(youthMemberInfo.getLunch())
                .dinner(youthMemberInfo.getDinner())
                .build();
    }

    public static MemberInfoResponseDto toMemberInfoResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .role(member.getRole())
                .birth(member.getBirth())
                .name(member.getName())
                .gender(member.getGender())
                .profileImage(member.getProfileImage())
                .build();
    }

    public static MemberInfoResponseDto toMemberInfoResponseDto(Member member, YouthMemberInfoDto youthMemberInfoDto) {
        return MemberInfoResponseDto.builder()
                .role(member.getRole())
                .birth(member.getBirth())
                .name(member.getName())
                .gender(member.getGender())
                .profileImage(member.getProfileImage())
                .youthMemberInfoDto(youthMemberInfoDto)
                .build();
    }
}
