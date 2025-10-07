package az.qala.permissionbased.repository;

import az.qala.permissionbased.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
