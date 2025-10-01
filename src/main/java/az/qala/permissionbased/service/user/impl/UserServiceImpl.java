package az.qala.permissionbased.service.user.impl;

import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.exception.DataExistsException;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.mapper.UserMapper;
import az.qala.permissionbased.model.dto.UserDTO;
import az.qala.permissionbased.model.entity.Role;
import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.enums.UserRoles;
import az.qala.permissionbased.model.request.auth.RegisterResponse;
import az.qala.permissionbased.model.response.GenericResponse;
import az.qala.permissionbased.repository.RoleRepository;
import az.qala.permissionbased.repository.UserRepository;
import az.qala.permissionbased.service.user.UserService;
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
    private final UserMapper userMapper;

    @Override
    public GenericResponse<UserDTO> createUser(RegisterResponse registerResponse) {
        userRepository.findByEmail(registerResponse.getEmail())
                .ifPresent(user -> {
                    throw new DataExistsException(ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(registerResponse.getEmail()));
                });

        userRepository.findByUsername(registerResponse.getUsername())
                .ifPresent(user -> {
                    throw new DataExistsException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(registerResponse.getUsername()));
                });


        User user = new User(
                registerResponse.getUsername(),
                registerResponse.getEmail(),
                passwordEncoder.encode(registerResponse.getPassword())
        );

        List<UserRoles> requestRoles = registerResponse.getRoles();

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

        UserDTO userDTO = userMapper.userToUserDto(user);

        return GenericResponse.success("User created", userDTO, 200);
    }
}
