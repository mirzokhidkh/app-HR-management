package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.Task;
import uz.mk.apphrmanagement.entity.Turniket;

import java.util.UUID;

@RepositoryRestResource(path = "turniket",collectionResourceRel = "list")
public interface TurniketRepository extends JpaRepository<Turniket, Integer> {
}
