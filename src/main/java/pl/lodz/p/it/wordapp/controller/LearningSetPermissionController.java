package pl.lodz.p.it.wordapp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.PermissionsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.exception.PermissionManagementAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.PermissionSelfManagementException;
import pl.lodz.p.it.wordapp.exception.UserNotFoundException;
import pl.lodz.p.it.wordapp.service.LearningSetPermissionService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sets/{setId}/permissions")
@SecurityRequirement(name = "bearerAuth")
public class LearningSetPermissionController {
    private final LearningSetPermissionService permissionService;

    @GetMapping
    public PermissionsDto getPermissions(@PathVariable Long setId) {
        return permissionService.getPermissions(setId);
    }

    @PutMapping("/{userId}/read")
    public void addReadPermission(@PathVariable Long setId,
                                  @PathVariable Long userId)
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        permissionService.addReadPermission(setId, userId);
    }

    @PutMapping("/{userId}/propose")
    public void addProposePermission(@PathVariable Long setId,
                                     @PathVariable Long userId)
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        permissionService.addProposePermission(setId, userId);
    }

    @PutMapping("/{userId}/edit")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void addEditPermission(@PathVariable Long setId,
                                  @PathVariable Long userId) {
        // TODO
    }

    @DeleteMapping("/{userId}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReadPermission(@PathVariable Long setId,
                                     @PathVariable Long userId)
        throws PermissionSelfManagementException, PermissionManagementAccessForbiddenException {
        permissionService.deleteReadPermission(setId, userId);
    }

    @DeleteMapping("/{userId}/propose")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProposePermission(@PathVariable Long setId,
                                        @PathVariable Long userId)
        throws PermissionManagementAccessForbiddenException {
        permissionService.deleteProposePermission(setId, userId);
    }

    @DeleteMapping("/{userId}/edit")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEditPermission(@PathVariable Long setId,
                                     @PathVariable Long userId) {
        // TODO
    }
}
