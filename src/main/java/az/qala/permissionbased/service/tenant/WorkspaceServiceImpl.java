package az.qala.permissionbased.service.tenant;

import az.qala.permissionbased.model.entity.Workspace;
import az.qala.permissionbased.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl {
    private final WorkspaceRepository repo;

    public Optional<Workspace> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<Workspace> create(Workspace workspace) {
        Workspace saved = repo.save(workspace);
        return Optional.ofNullable(saved);
    }
}
