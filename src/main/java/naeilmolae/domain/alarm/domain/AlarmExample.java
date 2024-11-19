package naeilmolae.domain.alarm.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AlarmExample {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Alarm alarm;

    private String content;

}
