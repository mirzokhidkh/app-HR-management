package uz.mk.apphrmanagement.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mk.apphrmanagement.entity.enums.TaskStatusName;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TaskStatusName taskStatusName;

    public TaskStatus(TaskStatusName taskStatusName) {
        this.taskStatusName = taskStatusName;
    }
}
