package az.qala.permissionbased.repository.impl;

import az.qala.permissionbased.repository.TenantAwareRepository;
import az.qala.permissionbased.tenant.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

// T - Entity class tpye; e.g. may be T = Workspace entity
// ID - is the type of the primary key for that entity

public class TenantAwareRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements TenantAwareRepository<T, ID> {

    private final EntityManager em; // jpa's api for interacting with database
    private final Class<T> domainClass; // depending on which class we operate (Workspace etc)

    public TenantAwareRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        // JpaEntityInformation - object that contains metadata about the entity (T), ? is id (PK)
        super(entityInformation, entityManager); //  calls the parent constructor to set up the normal repository behavior.

        this.em = entityManager; // storing EntityManager locally so we can use it later to build tenant-aware queries
        this.domainClass = (Class<T>) entityInformation.getJavaType(); // returns the entity class (like Workspace.class)
    }

    private void assertTenant() {
        if (TenantContext.getTenantid() == null) throw new IllegalStateException("TENANT NOT SET");
    }

    private Predicate tenantPredicate(CriteriaBuilder cb, Root<T> root) {
        return cb.equal(root.get("tenantId"), TenantContext.getTenantid());
    }

    public Optional<T> findById(ID id) {
        assertTenant();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);

        // select * from domainClass (T)
        Root<T> root = criteriaQuery.from(domainClass);
        // prepare where clause
        Predicate idPredicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)); // could do "%" + id + "%"

        // By passing root, you’re saying select the whole entity.
        // If you wanted only a column, you’d pass something like criteriaQuery.select(root.get("id")).
        criteriaQuery.select(root).where(idPredicate, tenantPredicate(criteriaBuilder, root));

        TypedQuery<T> q = em.createQuery(criteriaQuery);
        List<T> results = q.getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }


}
