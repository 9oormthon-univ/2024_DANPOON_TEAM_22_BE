package naeilmolae.domain.member.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import naeilmolae.domain.member.domain.Role;
import naeilmolae.domain.member.dto.request.MemberInfoRequestDto;
import naeilmolae.domain.member.validation.annotation.ValidRoleBasedRequest;

public class RoleBasedValidator implements ConstraintValidator<ValidRoleBasedRequest, MemberInfoRequestDto> {

    @Override
    public boolean isValid(MemberInfoRequestDto request, ConstraintValidatorContext context) {
        if (request.role() == Role.HELPER && request.youthMemberInfoDto() != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("조력자는 YouthMemberInfo를 포함할 수 없습니다.")
                    .addConstraintViolation();
            return false;
        }
        if (request.role() == Role.YOUTH && request.youthMemberInfoDto() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("청년은 YouthMemberInfo가 필요합니다.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
