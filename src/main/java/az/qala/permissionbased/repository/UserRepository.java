package az.qala.permissionbased.repository;

import az.qala.permissionbased.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID uuid);

    @Query("""
                    SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END
                    FROM User u
                    JOIN u.tenants t
                    WHERE u.id = :userId AND t.id = :tenantId
            """)
    boolean userHasTenant(UUID userId, Long tenantId);
}
