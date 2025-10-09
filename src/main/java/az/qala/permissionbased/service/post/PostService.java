package az.qala.permissionbased.service.post;

import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.exception.DataExistsException;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.model.dto.PostDTO;
import az.qala.permissionbased.model.dto.TagDTO;
import az.qala.permissionbased.model.entity.Post;
import az.qala.permissionbased.model.entity.PostTag;
import az.qala.permissionbased.model.entity.Tag;
import az.qala.permissionbased.model.entity.composite.PostTagId;
import az.qala.permissionbased.model.enums.Colors;
import az.qala.permissionbased.model.request.post.EditPostRequest;
import az.qala.permissionbased.repository.PostRepository;
import az.qala.permissionbased.repository.PostTagRepository;
import az.qala.permissionbased.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final TagRepository tagRepo;
    private final PostTagRepository postTagRepo;

    private PostDTO toPostDto(Post p) {
        return PostDTO.builder()
                .title(p.getTitle())
                .description(p.getDescription())
                .tags(p.getPostTags().stream().map(pt -> toTagDto(pt.getTag())).collect(Collectors.toSet()))
                .build();
    }

    private TagDTO toTagDto(Tag t) {
        return new TagDTO(t.getName(), t.getDescription(), t.getColor());
    }

    public PostDTO createPost(String title, String description, Set<Long> tagIds) {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);

        postRepo.save(post); // saving post first to generate ID


        if (tagIds != null && !tagIds.isEmpty()) {
            Set<PostTag> postTags = tagIds.stream()
                    .map(tagId -> {
                        Tag tag = tagRepo.findById(tagId)
                                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.TAG_NOT_FOUND.getMessage(tagId)));

                        PostTag postTag = new PostTag();
                        postTag.setPost(post);
                        postTag.setTag(tag);
                        postTag.setId(new PostTagId(post.getId(), tag.getId()));

                        return postTag;
                    }).collect(Collectors.toSet());

            post.getPostTags().addAll(postTags);
        }

        return toPostDto(postRepo.save(post));
    }

    /**
     * Add a tag to a post with metadata
     *
     * @param postId
     * @param tagId
     * @return PostTag
     */
    public PostTag addTagToPost(Long postId, Long tagId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.POST_NOT_FOUND.getMessage(postId)));

        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.TAG_NOT_FOUND.getMessage(tagId)));

        PostTagId postTagId = new PostTagId(post.getId(), tag.getId());
        if (postTagRepo.existsById(postTagId)) {
            throw new DataExistsException(ApiErrorMessage.POST_TAG_ALREADY_EXISTS.getMessage(postId, tagId));
        }

        PostTag postTag = new PostTag();
        postTag.setPost(post);
        postTag.setTag(tag);
        postTag.setAddedAt(LocalDateTime.now());

        return postTagRepo.save(postTag);
    }

    public List<PostDTO> getPosts(int page, int size) {
        if (page < 0) {
            page = 1;
        }

        if (page == 0) {
            ++page;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepo.findAll(pageable);

        return posts.stream()
                .map(this::toPostDto).collect(Collectors.toList());
    }

    public PostDTO editPost(EditPostRequest editPostRequest, Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.POST_NOT_FOUND.getMessage(postId)));

        Optional.ofNullable(editPostRequest.getTitle()).ifPresent(post::setTitle);
        Optional.ofNullable(editPostRequest.getDescription()).ifPresent(post::setDescription);
        Optional.ofNullable(editPostRequest.getTagIds()).ifPresent(post::setPostTags);

        return toPostDto(post);
    }
}
