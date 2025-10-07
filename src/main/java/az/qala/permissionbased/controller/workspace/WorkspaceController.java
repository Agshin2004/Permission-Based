package az.qala.permissionbased.controller.workspace;

import az.qala.permissionbased.model.entity.Workspace;
import az.qala.permissionbased.service.tenant.WorkspaceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {
    @Autowired
    WorkspaceServiceImpl workspaceService;

    @GetMapping("/{id}")
    public ResponseEntity<Workspace> getById(@PathVariable Long id) {
        Workspace byId = workspaceService.findById(id);

        return ResponseEntity.ok(byId);
    }

    @PostMapping
    public ResponseEntity<Workspace> create(@RequestBody Workspace workspace) {
        Workspace created = workspaceService.create(workspace);

        return ResponseEntity.ok(created);
    }
}
