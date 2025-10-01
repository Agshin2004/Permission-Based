package az.qala.permissionbased.repository;

import az.qala.permissionbased.model.entity.Role;
import az.qala.permissionbased.model.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserRoles name);
}
