package az.qala.permissionbased.model.request.user;

import az.qala.permissionbased.model.enums.Gender;
import az.qala.permissionbased.model.enums.Socials;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

public class UserProfileUpdateRequest implements Serializable {
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private String profilePictureUrl;
    private String jobTitle;
    private String organization;
    // will be added like: "socialLinks": {"GITHUB": "..."}
    private Map<Socials, String> socialLinks;
}
