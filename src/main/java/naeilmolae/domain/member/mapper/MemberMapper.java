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

    public static MemberLoginResponseDto toMemberLoginResponseDto(final Member member, TokenInfo tokenInfo, boolean isServiceMember, Role role) {
        return MemberLoginResponseDto.builder()
                .memberId(member.getId())
                .accessToken(tokenInfo.accessToken())
                .refreshToken(tokenInfo.refreshToken())
                .isServiceMember(isServiceMember)
                .role(role)
                .build();
    }

    public static YouthMemberInfo toYouthMemberInfo(YouthMemberInfoDto dto) {
        if (dto == null) {
            return YouthMemberInfo.builder()
                    .wakeUpTime(DefaultTimeSettings.DEFAULT_WAKE_UP_TIME)
                    .sleepTime(DefaultTimeSettings.DEFAULT_SLEEP_TIME)
                    .breakfast(DefaultTimeSettings.DEFAULT_BREAKFAST_TIME)
                    .lunch(DefaultTimeSettings.DEFAULT_LUNCH_TIME)
                    .dinner(DefaultTimeSettings.DEFAULT_DINNER_TIME)
                    .build();
        }

        return YouthMemberInfo.builder()
                .wakeUpTime(dto.getWakeUpTime() != null ? dto.getWakeUpTime() : DefaultTimeSettings.DEFAULT_WAKE_UP_TIME)
                .sleepTime(dto.getSleepTime() != null ? dto.getSleepTime() : DefaultTimeSettings.DEFAULT_SLEEP_TIME)
                .breakfast(dto.getBreakfast() != null ? dto.getBreakfast() : DefaultTimeSettings.DEFAULT_BREAKFAST_TIME)
                .lunch(dto.getLunch() != null ? dto.getLunch() : DefaultTimeSettings.DEFAULT_LUNCH_TIME)
                .dinner(dto.getDinner() != null ? dto.getDinner() : DefaultTimeSettings.DEFAULT_DINNER_TIME)
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
