package az.qala.permissionbased.repository;

import az.qala.permissionbased.model.dto.TagDTO;
import az.qala.permissionbased.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
