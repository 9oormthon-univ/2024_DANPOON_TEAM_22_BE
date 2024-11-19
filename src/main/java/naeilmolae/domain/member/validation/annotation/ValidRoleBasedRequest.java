package naeilmolae.domain.member.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import naeilmolae.domain.member.validation.validator.RoleBasedValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleBasedValidator.class)
public @interface ValidRoleBasedRequest {
    String message() default "선택한 역할과 다른 정보가 입력되었습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
