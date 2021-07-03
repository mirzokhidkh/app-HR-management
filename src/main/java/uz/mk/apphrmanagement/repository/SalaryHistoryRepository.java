package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.SalaryHistory;

import java.util.UUID;

@RepositoryRestResource(path = "salaryHistory")
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, UUID> {
}
