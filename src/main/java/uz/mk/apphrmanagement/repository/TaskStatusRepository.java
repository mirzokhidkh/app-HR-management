package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.apphrmanagement.entity.TaskStatus;
import uz.mk.apphrmanagement.entity.enums.TaskStatusName;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus,Integer> {
    TaskStatus findByName(TaskStatusName name);
}
