package uz.mk.apphrmanagement.payload;

import lombok.Data;
import uz.mk.apphrmanagement.entity.TaskStatus;
import uz.mk.apphrmanagement.entity.User;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.UUID;

@Data
public class TaskDto {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Date expireDate;
//    @NotNull
//    private Integer taskStatusId;
    @NotNull
    private UUID userId;


}
