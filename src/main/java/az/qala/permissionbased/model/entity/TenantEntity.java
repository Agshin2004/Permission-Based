package az.qala.permissionbased.model.entity;

import az.qala.permissionbased.context.TenantContext;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;

// NOTE: MappedSuperclass entites do not get a table, all entites that extend it will have columns from this entity
@MappedSuperclass
@Data
public class TenantEntity {
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private String tenantId;

    @PrePersist
    public void ensureTenant() {
        if (tenantId == null) {
            String t = TenantContext.getTenantid();
            if (t == null) throw new IllegalStateException("TENANT NOT FOUND IN CONTEXT");
            this.tenantId = t;
        }
    }
}
