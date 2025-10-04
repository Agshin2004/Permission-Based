package az.qala.permissionbased.model.seeder;

import az.qala.permissionbased.model.entity.Role;
import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.enums.RegistrationStatus;
import az.qala.permissionbased.model.enums.UserRoles;
import az.qala.permissionbased.repository.RoleRepository;
import az.qala.permissionbased.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Order(2)
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        // first creating roles
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("user");
            user1.setEmail("user@mail.com");
            user1.setPassword(passwordEncoder.encode("baku2212"));
            user1.setRegistrationStatus(RegistrationStatus.ACTIVE);
            user1.setRoles(List.of(roleRepository.findByRoleName(UserRoles.USER).get()));

            User user2 = new User();
            user2.setUsername("admin");
            user2.setEmail("admin@mail.com");
            user2.setPassword(passwordEncoder.encode("baku2212"));
            user2.setRegistrationStatus(RegistrationStatus.ACTIVE);
            user2.setRoles(List.of(roleRepository.findByRoleName(UserRoles.ADMIN).get()));

            User user3 = new User();
            user3.setUsername("lawDept");
            user3.setEmail("lawdept@mail.com");
            user3.setPassword(passwordEncoder.encode("baku2212"));
            user3.setRegistrationStatus(RegistrationStatus.ACTIVE);
            user3.setRoles(List.of(roleRepository.findByRoleName(UserRoles.LAW_DEPT).get()));

            userRepository.saveAll(List.of(user1, user2, user3));
        }
    }
}
