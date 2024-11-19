package naeilmolae.domain.member.repository;

import naeilmolae.domain.member.doman.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
