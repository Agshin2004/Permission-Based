package az.qala.permissionbased.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "workspace")
@Data
public class Workspace extends TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workspace_name", nullable = false)
    private String name;
}
