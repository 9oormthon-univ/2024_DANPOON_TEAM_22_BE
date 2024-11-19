package naeilmolae.domain.member.doman;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("관리자", 0),
    YOUTH("청년", 1),
    HELPER("조력자", 1);

    private final String toKorean;
    private final int priority;
}
