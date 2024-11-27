package naeilmolae.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.dto.response.MemberIdResponseDto;
import naeilmolae.domain.member.dto.response.MemberInfoResponseDto;
import naeilmolae.domain.member.dto.response.MemberNumResponseDto;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.global.common.base.BaseResponse;
import naeilmolae.global.config.security.auth.CurrentMember;
import org.springframework.web.bind.annotation.*;

@Tag(name = "멤버 API", description = "회원 가입, 회원 정보 조회/수정 등 회원 관리 관련 작업 담당 API")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입 API", description = "최초 멤버 정보를 등록하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PostMapping
    public BaseResponse<MemberIdResponseDto> signUp(@CurrentMember Member member,
                                                    @Valid @RequestBody MemberInfoRequestDto request) {
        return BaseResponse.onSuccess(memberService.signUp(member, request));
    }

    @Operation(summary = "회원 탈퇴 API", description = "해당 유저 정보를 삭제하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @DeleteMapping
    public BaseResponse<MemberIdResponseDto> withdrawal(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.withdrawal(member));
    }

    @Operation(summary = "회원 정보 수정 API", description = "멤버 정보 수정하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PatchMapping
    public BaseResponse<MemberIdResponseDto> patchMemberIfo(@CurrentMember Member member,
                                                            @Valid @RequestBody MemberInfoRequestDto request) {
        return BaseResponse.onSuccess(memberService.updateMemberInfo(member, request));
    }

    @Operation(summary = "회원 정보 조회 API", description = "멤버 정보 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping
    public BaseResponse<MemberInfoResponseDto> getMemberIfo(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.getMemberInfo(member));
    }

    @Operation(summary = "청년 회원 수 조회 API(메인)", description = "청년 회원 수 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/youth-num")
    public BaseResponse<MemberNumResponseDto> getYouthMemberNum(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.getMemberNum(Role.YOUTH));
    }

    @Operation(summary = "조력자 회원 수 조회 API(메인)", description = "조력자 회원 수 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/helper-num")
    public BaseResponse<MemberNumResponseDto> getHelperMemberNum(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.getMemberNum(Role.HELPER));
    }
}
