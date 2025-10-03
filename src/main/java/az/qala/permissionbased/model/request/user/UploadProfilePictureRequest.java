package az.qala.permissionbased.model.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class UploadProfilePictureRequest implements Serializable {
    private String filename;
    @NotBlank(message = "file must be provided")
    private MultipartFile file;
}
