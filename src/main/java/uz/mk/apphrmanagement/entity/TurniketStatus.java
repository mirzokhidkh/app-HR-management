package uz.mk.apphrmanagement.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mk.apphrmanagement.entity.enums.TaskStatusName;
import uz.mk.apphrmanagement.entity.enums.TurniketStatusName;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TurniketStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TurniketStatusName turniketStatusName;

}
