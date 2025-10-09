package az.qala.permissionbased.service.tag;

import az.qala.permissionbased.model.dto.TagDTO;
import az.qala.permissionbased.model.entity.Tag;
import az.qala.permissionbased.model.enums.Colors;
import az.qala.permissionbased.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    private final TagRepository tagRepo;

    public TagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    public TagDTO createTag(String title, String description, Colors color) {
        Tag tag = new Tag();
        tag.setName(title);
        tag.setDescription(description);
        tag.setColor(color);

        tagRepo.save(tag);

        TagDTO tagDto = TagDTO.builder()
                .name(tag.getName())
                .description(tag.getDescription())
                .color(tag.getColor())
                .build();


        return tagDto;
    }
}
