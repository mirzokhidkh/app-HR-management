package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.Product;

import java.util.UUID;

@RepositoryRestResource(path = "product")
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
