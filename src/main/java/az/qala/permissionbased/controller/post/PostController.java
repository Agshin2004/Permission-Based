package az.qala.permissionbased.controller.post;

import az.qala.permissionbased.constants.ApplicationConstants;
import az.qala.permissionbased.model.dto.AddTagDTO;
import az.qala.permissionbased.model.dto.PostDTO;
import az.qala.permissionbased.model.dto.TagDTO;
import az.qala.permissionbased.model.entity.Post;
import az.qala.permissionbased.model.entity.PostTag;
import az.qala.permissionbased.model.entity.Tag;
import az.qala.permissionbased.model.request.post.CreatePostRequest;
import az.qala.permissionbased.model.request.post.EditPostRequest;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<GenericResponse<List<PostDTO>>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PostDTO> allPosts = postService.getPosts(page, size);

        GenericResponse<List<PostDTO>> response = GenericResponse.success(ApplicationConstants.SUCCESS, allPosts, HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<PostDTO>> getPost(@PathVariable Long id) {
        PostDTO postDto = postService.getPost(id);

        GenericResponse<PostDTO> response = GenericResponse.success(ApplicationConstants.SUCCESS, postDto, HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<GenericResponse<PostDTO>> createPost(@RequestBody CreatePostRequest createPostRequest) {
        PostDTO postDto = postService.createPost(createPostRequest.getTitle(), createPostRequest.getDescription(), createPostRequest.getTagIds());

        GenericResponse<PostDTO> response = GenericResponse.success(ApplicationConstants.SUCCESS, postDto, HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenericResponse<PostDTO>> editPost(@PathVariable Long id, @RequestBody EditPostRequest editPostRequest) {
        PostDTO postDto = postService.editPost(editPostRequest, id);

        GenericResponse<PostDTO> response = GenericResponse.success(ApplicationConstants.SUCCESS, postDto, HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);

        GenericResponse<Void> response = GenericResponse.success(ApplicationConstants.SUCCESS, null, HttpStatus.NO_CONTENT.value());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }


    @PostMapping("/add-tag")
    public ResponseEntity<GenericResponse<PostTag>> addTagToPost(@RequestBody AddTagDTO addTagDto) {
        PostTag postTag = postService.addTagToPost(addTagDto.postId(), addTagDto.tagId());

        GenericResponse<PostTag> response = GenericResponse.success(ApplicationConstants.SUCCESS, postTag, HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

}
