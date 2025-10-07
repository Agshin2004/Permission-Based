package az.qala.permissionbased.service.tenant;

import az.qala.permissionbased.constants.ApiErrorMessage;
import az.qala.permissionbased.exception.DataNotFoundException;
import az.qala.permissionbased.model.entity.Workspace;
import az.qala.permissionbased.repository.UserRepository;
import az.qala.permissionbased.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public Workspace findById(Long id) {
        return workspaceRepository.findById(id).orElseThrow(() -> new DataNotFoundException(ApiErrorMessage.NOT_FOUND.getMessage()));
    }

    public Workspace create(Workspace workspace) {
        return workspaceRepository.save(workspace);
    }


}
