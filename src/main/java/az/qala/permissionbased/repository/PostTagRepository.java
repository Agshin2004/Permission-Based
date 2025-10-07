package az.qala.permissionbased.repository;

import az.qala.permissionbased.model.entity.PostTag;
import az.qala.permissionbased.model.entity.composite.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
}
