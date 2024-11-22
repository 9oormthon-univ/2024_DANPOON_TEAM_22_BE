package naeilmolae.domain.alarm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class AlarmCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 카테고리 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // 부모 카테고리 참조
    private AlarmCategory parent;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<AlarmCategory> children = new ArrayList<>();

    private String title;

    // Utility methods
    public void addChild(AlarmCategory child) {
        child.parent = this;
        this.children.add(child);
    }

    public boolean isRoot() {
        return this.parent == null;
    }
}
