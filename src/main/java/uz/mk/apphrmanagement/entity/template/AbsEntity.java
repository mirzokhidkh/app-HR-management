package uz.mk.apphrmanagement.entity.template;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class AbsEntity {
    @Id
    @GeneratedValue
    private UUID id;
}
