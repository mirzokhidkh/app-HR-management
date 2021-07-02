package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.apphrmanagement.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);
}
