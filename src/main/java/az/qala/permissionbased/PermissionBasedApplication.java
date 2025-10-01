package az.qala.permissionbased;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class PermissionBasedApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermissionBasedApplication.class, args);
    }

}
