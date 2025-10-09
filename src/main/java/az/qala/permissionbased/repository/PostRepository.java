package az.qala.permissionbased.repository;

import az.qala.permissionbased.model.dto.PostDTO;
import az.qala.permissionbased.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Collectors;

public interface PostRepository extends JpaRepository<Post, Long> {

}
