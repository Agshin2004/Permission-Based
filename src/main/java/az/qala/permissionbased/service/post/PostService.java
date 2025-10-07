package az.qala.permissionbased.service.post;

import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.exception.DataExistsException;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.model.entity.Post;
import az.qala.permissionbased.model.entity.PostTag;
import az.qala.permissionbased.model.entity.Tag;
import az.qala.permissionbased.model.entity.composite.PostTagId;
import az.qala.permissionbased.model.enums.Colors;
import az.qala.permissionbased.repository.PostRepository;
import az.qala.permissionbased.repository.PostTagRepository;
import az.qala.permissionbased.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final TagRepository tagRepo;
    private final PostTagRepository postTagRepo;

    public Post createPost(String title, String description) {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);

        postRepo.save(post);

        return post;
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
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.POST_NOT_FOUND.getMessage(tagId)));

        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.TAG_NOT_FOUND.getMessage(postId)));

        PostTagId postTagId = new PostTagId();
        postTagId.setPostId(post.getId());
        postTagId.setTagId(tag.getId());

        if (postTagRepo.existsById(postTagId)) {
            throw new DataExistsException(ApiErrorMessage.POST_TAG_ALREADY_EXISTS.getMessage(postId, tagId));
        }

        PostTag postTag = new PostTag();
        postTag.setId(postTagId);
        postTag.setPost(post);
        postTag.setTag(tag);
        postTag.setAddedAt(LocalDateTime.now());

        post.getPostTags().add(postTag);
        tag.getPostTags().add(postTag);

        return postTagRepo.save(postTag);
    }

    public Tag createTag(String title, String description, Colors color) {
        Tag tag = new Tag();
        tag.setName(title);
        tag.setDescription(description);
        tag.setColor(color);

        tagRepo.save(tag);

        return tag;
    }
}
