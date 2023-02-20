package pl.lodz.p.it.wordapp.controller;

import java.util.List;
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
import pl.lodz.p.it.wordapp.controller.dto.UserPermissionsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetPermissionAccessForbiddenException;
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

    @GetMapping("/users/{userId}")
    public PermissionsDto getPermissionsForUser(@PathVariable Long setId,
                                                @PathVariable Long userId)
        throws LearningSetPermissionAccessForbiddenException {
        return permissionService.getPermissionsForUser(setId, userId);
    }

    @GetMapping("/users")
    public List<UserPermissionsDto> getPermissionsForUsers(@PathVariable Long setId)
        throws LearningSetPermissionAccessForbiddenException {
        return permissionService.getPermissionsForUsers(setId);
    }

    @PutMapping("/users/{userId}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addReadPermission(@PathVariable Long setId,
                                  @PathVariable Long userId)
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        permissionService.addReadPermission(setId, userId);
    }

    @PutMapping("/users/{userId}/propose")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addProposePermission(@PathVariable Long setId,
                                     @PathVariable Long userId)
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        permissionService.addProposePermission(setId, userId);
    }

    @PutMapping("/users/{userId}/edit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addEditPermission(@PathVariable Long setId,
                                  @PathVariable Long userId)
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        permissionService.addEditPermission(setId, userId);
    }

    @DeleteMapping("/users/{userId}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReadPermission(@PathVariable Long setId,
                                     @PathVariable Long userId)
        throws PermissionSelfManagementException, PermissionManagementAccessForbiddenException {
        permissionService.deleteReadPermission(setId, userId);
    }

    @DeleteMapping("/users/{userId}/propose")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProposePermission(@PathVariable Long setId,
                                        @PathVariable Long userId)
        throws PermissionManagementAccessForbiddenException, PermissionSelfManagementException {
        permissionService.deleteProposePermission(setId, userId);
    }

    @DeleteMapping("/users/{userId}/edit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEditPermission(@PathVariable Long setId,
                                     @PathVariable Long userId)
        throws PermissionSelfManagementException, PermissionManagementAccessForbiddenException {
        permissionService.deleteEditPermission(setId, userId);
    }
}
