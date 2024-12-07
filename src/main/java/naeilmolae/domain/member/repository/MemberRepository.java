package naeilmolae.domain.member.repository;

import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByClientIdAndLoginType(String clientId, LoginType loginType);
    Long countAllByRole(Role role);

    @Query("SELECT m FROM Member m JOIN FETCH m.youthMemberInfo y WHERE m.role = 'YOUTH'")
    List<Member> findAllYouthMembersWithInfo(@Param("role") Role role);

}
