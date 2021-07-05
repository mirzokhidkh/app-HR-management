package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.SalaryHistory;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "salaryHistory",collectionResourceRel = "list")
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, UUID> {

    List<SalaryHistory> findAllByDateBetween(Date minDate, Date maxDate);

    List<SalaryHistory> findAllByUserId(UUID user_id);
}
