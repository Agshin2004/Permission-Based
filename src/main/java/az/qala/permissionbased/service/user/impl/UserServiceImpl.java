package az.qala.permissionbased.service.user.impl;

import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.constants.ApplicationConstants;
import az.qala.permissionbased.exception.DataExistsException;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.model.dto.RoleDTO;
import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.entity.Role;
import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.enums.UserRoles;
import az.qala.permissionbased.model.request.auth.ApproveUserRequest;
import az.qala.permissionbased.model.request.auth.LoginRequest;
import az.qala.permissionbased.model.request.auth.RegisterRequest;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.model.response.auth.LoginResponse;
import az.qala.permissionbased.repository.RoleRepository;
import az.qala.permissionbased.repository.UserRepository;
import az.qala.permissionbased.service.user.UserService;
import az.qala.permissionbased.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
