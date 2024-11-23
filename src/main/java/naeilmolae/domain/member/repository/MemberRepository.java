package naeilmolae.domain.member.repository;

import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByClientIdAndLoginType(String clientId, LoginType loginType);
    Long countAllByRole(Role role);
}
