package az.qala.permissionbased.controller.post;

import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.model.entity.Post;
import az.qala.permissionbased.model.entity.PostTag;
import az.qala.permissionbased.model.entity.Tag;
import az.qala.permissionbased.model.enums.Colors;
import az.qala.permissionbased.repository.PostRepository;
import az.qala.permissionbased.repository.PostTagRepository;
import az.qala.permissionbased.repository.TagRepository;
import az.qala.permissionbased.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

record CreateTagDto(String name, Colors color, String description) {
}

record AddTagDto(Long postId, Long tagId) {}

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;


    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post postRequest) {
        Post post = postService.createPost(postRequest.getTitle(), postRequest.getDescription());

        return ResponseEntity.ok(post);
    }

    @PostMapping("/create-tag")
    public ResponseEntity<Tag> createTag(@RequestBody CreateTagDto dto) {
        System.out.println();
        Tag tag = postService.createTag(dto.name(), dto.description(), dto.color());
        return ResponseEntity.ok(tag);
    }

    @PostMapping("/add-tag")
    public ResponseEntity<?> addTagToPost(@RequestBody AddTagDto addTagDto) {
        PostTag saved = postService.addTagToPost(addTagDto.postId(), addTagDto.tagId());

        return ResponseEntity.ok(saved);
    }
}
