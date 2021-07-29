package uz.mk.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.mk.apphrmanagement.entity.template.AbsUUIDEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkTimeHistory extends AbsUUIDEntity {
    private Date date;

    private String entryTime;

    private String departureTime;

    @ManyToOne
    private User user;

}
