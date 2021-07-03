package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.Task;

import java.util.UUID;

@RepositoryRestResource(path = "task")
public interface TaskRepository extends JpaRepository<Task, UUID> {
}
