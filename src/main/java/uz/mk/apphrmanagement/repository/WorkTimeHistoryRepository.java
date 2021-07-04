package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.WorkTimeHistory;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "workTimeHistory", collectionResourceRel = "list")
public interface WorkTimeHistoryRepository extends JpaRepository<WorkTimeHistory, UUID> {
    Optional<WorkTimeHistory> findByDate(Date date);

    @Query(value = "select *\n" +
            "from work_time_history wth\n" +
            "         join users_work_time_history uwth on wth.id = uwth.work_time_history_id\n" +
            "where wth.date=?1 and uwth.users_id=?2", nativeQuery = true)
    Optional<WorkTimeHistory> findByDateAndUserId(Date date, UUID userId);

    @Query(value = "select case when count(wth)> 0\n" +
            "then true else false end from work_time_history wth\n" +
            "         join users_work_time_history uwth on wth.id = uwth.work_time_history_id\n" +
            "where wth.date=?1 and uwth.users_id=?2", nativeQuery = true)
    boolean existsByDateAndUserId(Date date, UUID userId);
}
