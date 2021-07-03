package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.mk.apphrmanagement.entity.User;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "user", collectionResourceRel = "list")
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);

    @Query(value = "select email from users where id =?1", nativeQuery = true)
    String getEmailById(UUID id);

    @Query(value = "select firstname,lastname from users where id =?1", nativeQuery = true)
    String getFullNameById(UUID id);

    @Query(value = "select distinct u.id,\n" +
            "                u.firstname,\n" +
            "                u.lastname,\n" +
            "                u.email,\n" +
            "                u.password,\n" +
            "                u.created_at,\n" +
            "                u.updated_at,\n" +
            "                u.account_non_expired,\n" +
            "                u.account_non_locked,\n" +
            "                u.credentials_non_expired,\n" +
            "                u.enabled,\n" +
            "                u.email_code\n" +
            "from users u\n" +
            "         join users_roles ur on u.id = ur.users_id\n" +
            "where ur.users_id = ?1\n" +
            "  and ur.roles_id = ?2", nativeQuery = true)
    Optional<User> findByIdAndRolesInByNative(UUID id, Integer roleId);


}
