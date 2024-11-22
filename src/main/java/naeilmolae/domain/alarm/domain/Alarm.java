package naeilmolae.domain.alarm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alarm_category_id")
    private AlarmCategory alarmCategory;


    public Alarm(AlarmCategory alarmCategory) {
        if (alarmCategory.isRoot()) {
            throw new IllegalArgumentException("AlarmCategory must be root category");
        }
        this.alarmCategory = alarmCategory;
    }
}
