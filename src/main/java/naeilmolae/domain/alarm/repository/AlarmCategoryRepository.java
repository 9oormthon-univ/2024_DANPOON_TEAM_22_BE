package naeilmolae.domain.alarm.repository;

import naeilmolae.domain.alarm.domain.AlarmCategory;
import naeilmolae.domain.alarm.domain.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmCategoryRepository extends JpaRepository<AlarmCategory, Long> {

    @Query("SELECT ac " +
            "FROM AlarmCategory ac " +
            "JOIN FETCH ac.children " +
            "WHERE ac.categoryType = :categoryType " +
            "AND ac.parent IS NULL")
    List<AlarmCategory> findByCategoryTypeAndParentIsNull(CategoryType categoryType);

//
}
