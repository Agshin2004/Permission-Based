package az.qala.permissionbased.model.entity;

import az.qala.permissionbased.model.entity.mappedClasses.TenantEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "workspace",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "workspace_name"}))
@Data
public class Workspace extends TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workspace_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
