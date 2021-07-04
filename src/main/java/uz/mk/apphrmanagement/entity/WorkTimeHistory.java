package uz.mk.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;
import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkTimeHistory {
    @Id
    @GeneratedValue
    private UUID id;

    private Date date;

    private String entryTime;

    private String departureTime;

    @ManyToOne
    private User user;

}
