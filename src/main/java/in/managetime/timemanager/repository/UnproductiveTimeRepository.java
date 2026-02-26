package in.managetime.timemanager.repository;

import in.managetime.timemanager.entity.UnproductiveTimeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface UnproductiveTimeRepository extends JpaRepository<UnproductiveTimeEntity, Long> {

    //select * from tbl_UnproductiveTime where profile_id = ?1 order by date desc
    List<UnproductiveTimeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    //select * from tbl_UnproductiveTime where profile_id = ?1 order by date desc limit 5
    List<UnproductiveTimeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(i.time) FROM UnproductiveTimeEntity i WHERE i.profile.id = :profileId")
    BigDecimal findTotalUnproductiveTimeByProfileId(@Param("profileId") Long profileId);

    //select * from tbl_UnproductiveTime where profile_id = ?1 and date between ?2 and ?3 and name like %?4%
    List<UnproductiveTimeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    //select * from tbl_UnproductiveTime where profile_id = ?1 and date between ?2 and ?3
    List<UnproductiveTimeEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}
