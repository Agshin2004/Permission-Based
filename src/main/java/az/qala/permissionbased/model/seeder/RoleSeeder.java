package az.qala.permissionbased.model.seeder;

import az.qala.permissionbased.model.entity.Role;
import az.qala.permissionbased.model.enums.UserRoles;
import az.qala.permissionbased.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class RoleSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setRoleName(UserRoles.USER);
            userRole.setCreatedAt(LocalDateTime.now());
            userRole.setActive(true);

            Role adminRole = new Role();
            adminRole.setRoleName(UserRoles.ADMIN);
            adminRole.setCreatedAt(LocalDateTime.now());
            adminRole.setActive(true);

            Role lawDeptRole = new Role();
            lawDeptRole.setRoleName(UserRoles.LAW_DEPT);
            lawDeptRole.setCreatedAt(LocalDateTime.now());
            lawDeptRole.setActive(true);

            roleRepository.saveAll(List.of(userRole, adminRole, lawDeptRole));
        }
    }
}
