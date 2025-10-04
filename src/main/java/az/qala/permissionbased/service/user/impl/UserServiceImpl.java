package az.qala.permissionbased.service.user.impl;

import az.qala.permissionbased.config.CustomUserDetails;
import az.qala.permissionbased.config.CustomUserDetailsService;
import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.constants.ApplicationConstants;
import az.qala.permissionbased.exception.DataExistsException;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.model.dto.RoleDTO;
import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.entity.Role;
import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.entity.UserProfile;
import az.qala.permissionbased.model.enums.UserRoles;
import az.qala.permissionbased.model.request.user.*;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.model.response.user.LoginResponse;
import az.qala.permissionbased.repository.RoleRepository;
import az.qala.permissionbased.repository.UserRepository;
import az.qala.permissionbased.service.user.UserService;
import az.qala.permissionbased.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public GenericResponse<UserDTO> createUser(RegisterRequest registerRequest) {
        userRepository.findByEmail(registerRequest.getEmail())
                .ifPresent(user -> {
                    throw new DataExistsException(ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(registerRequest.getEmail()));
                });

        userRepository.findByUsername(registerRequest.getUsername())
                .ifPresent(user -> {
                    throw new DataExistsException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(registerRequest.getUsername()));
                });


        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        List<UserRoles> requestRoles = registerRequest.getRoles();
        List<Role> roles = new ArrayList<>();

        if (requestRoles == null) {
            requestRoles = List.of(UserRoles.USER);
        }

        for (UserRoles r : requestRoles) {
            Role role = roleRepository.findByRoleName(r)
                    .orElseThrow(() -> new DataNotFoundException(
                            ApiErrorMessage.ROLE_NOT_FOUND.getMessage(r.getRole())
                    ));

            roles.add(role);
        }

        user.setRoles(roles);
        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);

        String jwt = jwtUtils.generateToken(user);

        List<RoleDTO> roleDtos = roles.stream().map((role) -> {
                    return new RoleDTO(role.getId(), role.getRoleName());
                })
                .toList();

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .registrationStatus(user.getRegistrationStatus())
                .jwt(jwt)
                .roles(roleDtos)
                .build();

        return GenericResponse.success("User created successfully", userDTO, 200);
    }

    public GenericResponse<LoginResponse> loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.WRONG_CREDENTIALS.getMessage()));

        String jwt = jwtUtils.generateToken(user);

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new DataNotFoundException(ApiErrorMessage.WRONG_CREDENTIALS.getMessage());
        }

        LoginResponse response = new LoginResponse(jwt);

        return GenericResponse.success(ApplicationConstants.SUCCESS, response, 200);
    }

    public GenericResponse<String> approveUser(ApproveUserRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.USER_NOT_FOUND.getMessage(request.getUserId().toString()))); // check without toString

        user.setRegistrationStatus(request.getRegistrationStatus());

        return GenericResponse.success(ApplicationConstants.SUCCESS, "User status set to " + request.getRegistrationStatus().name(), 200);
    }


    @Override
    public GenericResponse<Map<String, String>> saveProfilePicture(
            UploadProfilePictureRequest uploadProfilePictureRequest,
            CustomUserDetails userDetails) {

        MultipartFile file = uploadProfilePictureRequest.getFile();
        String filename = uploadProfilePictureRequest.getFilename();

        User user = userDetails.getUser();

        try {
            String uploadDir = new File("uploads").getAbsolutePath();
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }


            String[] extSplit = file.getOriginalFilename().split("\\.");
            String fileExt; // not initializing, only local variables throw error when we wanna access it before initilizing, fields are initialized to null by default

            if (extSplit.length > 1) {
                fileExt = extSplit[1];
            } else {
                fileExt = "jpg";
            }

            String filepath = uploadDir + File.separator + (filename != null ? filename.concat(".").concat(fileExt) : file.getOriginalFilename());

            file.transferTo(new File(filepath));

            user.getProfile().setProfilePictureUrl(filepath);
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("uploadPath", filepath);

            return GenericResponse.success(ApplicationConstants.SUCCESS, response, HttpStatus.OK.value());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GenericResponse<Map<String, Boolean>> addUserProfileDetails(
            UserProfileUpdateRequest userProfileUpdateRequest,
            CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        UserProfile profile = user.getProfile();

        // NOT USING MAPSTRUCT ON PURPOSE IN THIS PROJECT!
        Optional.ofNullable(userProfileUpdateRequest.getFirstName()).ifPresent(profile::setFirstName);
        Optional.ofNullable(userProfileUpdateRequest.getLastName()).ifPresent(profile::setLastName);
        Optional.ofNullable(userProfileUpdateRequest.getDateOfBirth()).ifPresent(profile::setDateOfBirth);
        Optional.ofNullable(userProfileUpdateRequest.getGender()).ifPresent(profile::setGender);
        Optional.ofNullable(userProfileUpdateRequest.getPhoneNumber()).ifPresent(profile::setPhoneNumber);
        Optional.ofNullable(userProfileUpdateRequest.getAddress()).ifPresent(profile::setAddress);
        Optional.ofNullable(userProfileUpdateRequest.getCity()).ifPresent(profile::setCity);
        Optional.ofNullable(userProfileUpdateRequest.getPostalCode()).ifPresent(profile::setPostalCode);
        Optional.ofNullable(userProfileUpdateRequest.getJobTitle()).ifPresent(profile::setJobTitle);
        Optional.ofNullable(userProfileUpdateRequest.getOrganization()).ifPresent(profile::setOrganization);


        if (userProfileUpdateRequest.getSocialLinks() != null) {
            if (profile.getSocialLinks() == null) {
                profile.setSocialLinks(new HashMap<>());
            }

            userProfileUpdateRequest.getSocialLinks().forEach((k, v) -> {
                if (v != null) {
                        profile.getSocialLinks().put(k, v);
                }
            });
        }

        userRepository.save(user);

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);

        return GenericResponse.success("success", response, HttpStatus.OK.value());

    }
}
