package az.qala.permissionbased.controller.tag;

import az.qala.permissionbased.model.dto.TagDTO;
import az.qala.permissionbased.model.entity.Tag;
import az.qala.permissionbased.model.request.tag.TagRequest;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @RequestMapping
    public ResponseEntity<GenericResponse<TagDTO>> createTag(@RequestBody TagRequest tagRequest) {
        TagDTO tag = tagService.createTag(tagRequest.getName(), tagRequest.getDescription(), tagRequest.getColor());

        GenericResponse<TagDTO> response = GenericResponse.success("success", tag, HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }
}
