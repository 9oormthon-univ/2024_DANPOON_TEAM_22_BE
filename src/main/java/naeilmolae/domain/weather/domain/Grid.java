package naeilmolae.domain.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grid {
    @Id
    @GeneratedValue
    private Long id;

    private Integer x;
    private Integer y;

    public Grid(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

}
