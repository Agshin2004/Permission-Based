package az.qala.permissionbased;

import az.qala.permissionbased.repository.impl.TenantAwareRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// annotating so spring will use this class for TenantAwareRepository and all subclasses (like Workspace repo)
@EnableJpaRepositories(
        repositoryBaseClass = TenantAwareRepositoryImpl.class
)
@SpringBootApplication
public class PermissionBasedApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermissionBasedApplication.class, args);
    }

}
