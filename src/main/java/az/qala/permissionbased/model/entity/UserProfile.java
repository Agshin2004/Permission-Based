package az.qala.permissionbased.model.entity;

import az.qala.permissionbased.converter.SocialLinksConverter;
import az.qala.permissionbased.model.enums.Gender;
import az.qala.permissionbased.model.enums.Socials;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "organization")
    private String organization;

    // convert is a JPA annotation used to define custom conversion logic between java types and db types
    // You use it with a class that implements the jakarta.persistence.AttributeConverter<X, Y> interface, where:
    // x is the java type in entity
    // y is the database column type
    @Convert(converter = SocialLinksConverter.class)
    @Column(name = "social_links", columnDefinition = "TEXT")
    private Map<Socials, String> socialLinks;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;
}
