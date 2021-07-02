package uz.mk.apphrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);
}
