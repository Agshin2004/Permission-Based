package az.qala.permissionbased.repository.impl;

import az.qala.permissionbased.model.entity.TenantEntity;
import az.qala.permissionbased.repository.TenantAwareRepository;
import az.qala.permissionbased.context.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Optional<T> findById(ID id) {
        assertTenant();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);

        // select * from domainClass (T)
        Root<T> root = criteriaQuery.from(domainClass);
        // prepare where clause
        Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id); // could do "%" + id + "%"

        // from(domainClass) → tells Hibernate where the data comes from.
        // select(root) → tells Hibernate what to actually return; you’d pass something like criteriaQuery.select(root.get("id")).
        criteriaQuery.select(root).where(idPredicate, tenantPredicate(criteriaBuilder, root));

        TypedQuery<T> q = em.createQuery(criteriaQuery);
        List<T> results = q.getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public <S extends T> S save(S entity) {
        // just security checks
        if (entity instanceof TenantEntity) {
            // casting to TenantEntity since S extends T and may be parent class
            TenantEntity te = (TenantEntity) entity;
            String current = TenantContext.getTenantid();
            if (te.getTenantId() == null) {
                te.setTenantId(current);
            } else if (!te.getTenantId().equals(current)) {
                throw new SecurityException("Entity tenant mismatch!!!");
            }
        }

        return super.save(entity);
    }


    @Override
    public List<T> findAll() {
        assertTenant();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(domainClass);
        Root<T> root = cq.from(domainClass);

        cq.select(root).where(tenantPredicate(cb, root));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        assertTenant();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // content query
        CriteriaQuery<T> cq = cb.createQuery(domainClass);
        Root<T> root = cq.from(domainClass); // root essentially is just entity
        cq.select(root).where(tenantPredicate(cb, root));
        TypedQuery<T> tq = em.createQuery(cq);

        tq.setFirstResult((int) pageable.getOffset());
        tq.setMaxResults(pageable.getPageSize());
        List<T> content = tq.getResultList();


        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(domainClass);
        countQuery.select(cb.count(countRoot)).where(cb.equal(countRoot.get("tenantId"), TenantContext.getTenantid()));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public void deleteById(ID id) {
        Optional<T> entity = findById(id);
        if (entity.isEmpty()) throw new IllegalArgumentException("ENTITY NOT FOUND");

        super.delete(entity.get());
    }
}
