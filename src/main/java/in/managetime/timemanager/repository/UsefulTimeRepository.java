package in.managetime.timemanager.repository;

import in.managetime.timemanager.entity.UsefulTimeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface UsefulTimeRepository extends JpaRepository<UsefulTimeEntity, Long> {

    //select * from tbl_expenses where profile_id = ?1 order by date desc
    List<UsefulTimeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    //select * from tbl_UsefulTime where profile_id = ?1 order by date desc limit 5
    List<UsefulTimeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(e.time) FROM UsefulTimeEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalUsefulTimeByProfileId(@Param("profileId") Long profileId);

    //select * from tbl_UsefulTime where profile_id = ?1 and date between ?2 and ?3 and name like %?4%
    List<UsefulTimeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );


    //select * from tbl_UsefulTime where profile_id = ?1 and date between ?2 and ?3
    List<UsefulTimeEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    //select * from tbl_UsefulTime where profile_id = ?1 and date = ?2
    List<UsefulTimeEntity> findByProfileIdAndDate(Long profileId, LocalDate date);
}
