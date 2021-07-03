package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.WorkTimeHistory;

import java.util.UUID;

@RepositoryRestResource(path = "workTimeHistory",collectionResourceRel = "list")
public interface WorkTimeHistoryRepository extends JpaRepository<WorkTimeHistory, UUID> {

}
