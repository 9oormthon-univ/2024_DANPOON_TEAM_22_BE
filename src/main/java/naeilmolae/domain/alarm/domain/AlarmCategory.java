package naeilmolae.domain.alarm.domain;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum AlarmCategory {
    // 루트 카테고리들
    WAKE_UP("기상", CategoryType.DAILY, null),
    GO_OUT("외출", CategoryType.DAILY, null),
    MEAL("식사", CategoryType.DAILY, null),
    SLEEP("취침", CategoryType.DAILY, null),
    SADNESS("우울과 불안", CategoryType.COMFORT, null),
    PRAISE("칭찬과 격려", CategoryType.COMFORT, null),
    CONSOLATION("위로", CategoryType.COMFORT, null),
    INFO("정보", CategoryType.INFO, null),

    // 자식 카테고리들 - WAKE_UP의 자식들
    WAKE_UP_WEEKDAY("아침 기상 (평일)", CategoryType.DAILY, WAKE_UP),
    WAKE_UP_WEEKEND("아침 기상 (주말)", CategoryType.DAILY, WAKE_UP),

    // 자식 카테고리들 - GO_OUT의 자식들
    GO_OUT_CLEAR("날씨가 맑을 때", CategoryType.DAILY, GO_OUT),
    GO_OUT_RAIN("비가 올 때", CategoryType.DAILY, GO_OUT),
    GO_OUT_SNOW("눈이 올 때", CategoryType.DAILY, GO_OUT),
    GO_OUT_COLD("날씨가 추울 때", CategoryType.DAILY, GO_OUT),
    GO_OUT_HOT("날씨가 더울 때", CategoryType.DAILY, GO_OUT),

    // 자식 카테고리들 - MEAL의 자식들
    MEAL_BREAKFAST("아침 식사 알림", CategoryType.DAILY, MEAL),
    MEAL_LUNCH("점심 식사 알림", CategoryType.DAILY, MEAL),
    MEAL_DINNER("저녁 식사 알림", CategoryType.DAILY, MEAL),

    // 자식 카테고리들 - SLEEP의 자식들
    SLEEP_PREPARE("취침 준비", CategoryType.DAILY, SLEEP),

    // 자식 카테고리들 - SADNESS의 자식들
    SADNESS_ALONE("혼자 있을 때", CategoryType.COMFORT, SADNESS),
    SADNESS_BEFORE_SLEEP("밤에 잠들기 전", CategoryType.COMFORT, SADNESS),
    SADNESS_LONELY("외로움을 느낄 때", CategoryType.COMFORT, SADNESS),
    SADNESS_OVERWHELMED("막막함을 느낄 때", CategoryType.COMFORT, SADNESS),
    SADNESS_UNCERTAIN_FUTURE("미래가 불안할 때", CategoryType.COMFORT, SADNESS),

    // 자식 카테고리들 - PRAISE의 자식들
    PRAISE_SMALL_ACHIEVEMENT("작은 성취를 이루었을 때", CategoryType.COMFORT, PRAISE),
    PRAISE_EFFORTING("목표를 향해 노력 중일 때", CategoryType.COMFORT, PRAISE),
    PRAISE_OVERCOME("어려움을 극복해냈을 때", CategoryType.COMFORT, PRAISE),
    PRAISE_PERSISTENCE("꾸준히 노력 중일 때", CategoryType.COMFORT, PRAISE),
    PRAISE_TIRED("지쳐 있을 때", CategoryType.COMFORT, PRAISE),

    // 자식 카테고리들 - CONSOLATION의 자식들
    CONSOLATION_MISTAKE("실수를 했을 때", CategoryType.COMFORT, CONSOLATION),
    CONSOLATION_PLAN_FAIL("계획대로 되지 않았을 때", CategoryType.COMFORT, CONSOLATION),
    CONSOLATION_FRUSTRATION("좌절감을 느낄 때", CategoryType.COMFORT, CONSOLATION),
    CONSOLATION_FATIGUE("피로를 느낄 때", CategoryType.COMFORT, CONSOLATION),
    CONSOLATION_INADEQUATE("스스로가 부족하다고 느낄 때", CategoryType.COMFORT, CONSOLATION),

    INFO_INFO("정보", CategoryType.DAILY, INFO);

    private final String name;
    private final CategoryType categoryType;
    private final AlarmCategory parent;

    // 모든 루트 카테고리 상수로 저장
    public static final List<AlarmCategory> ROOT_CATEGORIES;

    static {
        // 루트 카테고리 초기화
        ROOT_CATEGORIES = Stream.of(AlarmCategory.values())
                .filter(AlarmCategory::isRoot)
                .collect(Collectors.toList());
    }

    AlarmCategory(String name, CategoryType categoryType, AlarmCategory parent) {
        this.name = name;
        this.categoryType = categoryType;
        this.parent = parent;
    }

    public static List<AlarmCategory> getByParent(AlarmCategory parent) {
        return List.of(AlarmCategory.values()).stream()
                .filter(category -> category.isChildOf(parent))
                .toList();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isChildOf(AlarmCategory parentCategory) {
        return this.parent == parentCategory;
    }

    public static List<AlarmCategory> getChildrenByParent(AlarmCategory parent) {
        return Stream.of(AlarmCategory.values())
                .filter(category -> category.isChildOf(parent))
                .collect(Collectors.toList());
    }
}
