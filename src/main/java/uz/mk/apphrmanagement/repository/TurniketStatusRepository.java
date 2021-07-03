package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.apphrmanagement.entity.TurniketStatus;
import uz.mk.apphrmanagement.entity.enums.TurniketStatusName;

@Repository
public interface TurniketStatusRepository extends JpaRepository<TurniketStatus,Integer> {
    TurniketStatus findByName(TurniketStatusName name);
}
