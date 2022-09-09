package pl.lodz.p.it.wordapp.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.controller.dto.PermissionsDto;
import pl.lodz.p.it.wordapp.controller.dto.UserPermissionsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetPermissionAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.PermissionManagementAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.PermissionSelfManagementException;
import pl.lodz.p.it.wordapp.exception.UserNotFoundException;
import pl.lodz.p.it.wordapp.model.AccessRole;
import pl.lodz.p.it.wordapp.model.Account;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.model.Role;
import pl.lodz.p.it.wordapp.repository.AccessRoleRepository;
import pl.lodz.p.it.wordapp.repository.AccountRepository;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@Service
@RequiredArgsConstructor
public class LearningSetPermissionService {
    private final AccessRoleRepository accessRoleRepository;
    private final LearningSetRepository learningSetRepository;
    private final AccountRepository accountRepository;

    /**
     * Retrieves permission for current user from the database.
     *
     * @param setId ID of the learning set.
     * @return Object with user's permissions.
     */
    public PermissionsDto getPermissions(Long setId) {
        Long userId = AccountService.getCurrentUserId();

        return accessRoleRepository
            .findBySet_IdAndUser_Id(setId, userId)
            .map(PermissionsDto::new)
            .orElseGet(
                () -> learningSetRepository
                    .findDistinctById(setId)
                    .filter(LearningSetDetailsDto::isPubliclyVisible)
                    .map(dto -> new PermissionsDto(Role.READER))
                    .orElseGet(PermissionsDto::new)
            );
    }

    public PermissionsDto getPermissionsForUser(Long setId, Long userId)
        throws LearningSetPermissionAccessForbiddenException {
        if (!isOwner(AccountService.getCurrentUserId(), setId)) {
            throw new LearningSetPermissionAccessForbiddenException();
        }

        return accessRoleRepository
            .findBySet_IdAndUser_Id(setId, userId)
            .map(PermissionsDto::new)
            .orElseGet(() -> learningSetRepository
                .findDistinctById(setId)
                .filter(LearningSetDetailsDto::isPubliclyVisible)
                .map(dto -> new PermissionsDto(Role.READER))
                .orElseGet(PermissionsDto::new)
            );
    }

    public List<UserPermissionsDto> getPermissionsForUsers(Long setId)
        throws LearningSetPermissionAccessForbiddenException {
        if (!isOwner(AccountService.getCurrentUserId(), setId)) {
            throw new LearningSetPermissionAccessForbiddenException();
        }

        return accessRoleRepository
            .findBySet_IdOrderById(setId)
            .stream()
            .filter(ar -> ar.getRole() != Role.OWNER)
            .map(UserPermissionsDto::new)
            .toList();
    }

    // region READ permission
    @Transactional
    public void addReadPermission(Long setId, Long userId)
        throws PermissionManagementAccessForbiddenException, LearningSetNotFoundException, UserNotFoundException {
        Long requestAuthorId = AccountService.getCurrentUserId();

        if (isOwner(requestAuthorId, setId)) {
            Optional<AccessRole> accessRole = accessRoleRepository
                .findBySet_IdAndUser_Id(setId, userId);

            if (accessRole.isEmpty()) {
                LearningSet ls = learningSetRepository
                    .findById(setId)
                    .orElseThrow(() -> new LearningSetNotFoundException(setId));
                Account acc = accountRepository
                    .findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));

                accessRoleRepository.save(new AccessRole(Role.READER, ls, acc));
            }
        } else {
            throw new PermissionManagementAccessForbiddenException();
        }
    }

    @Transactional
    public void deleteReadPermission(Long setId, Long userId)
        throws PermissionManagementAccessForbiddenException, PermissionSelfManagementException {
        Long requestAuthorId = AccountService.getCurrentUserId();

        if (Objects.equals(requestAuthorId, userId)) {
            throw new PermissionSelfManagementException();
        }

        if (isOwner(requestAuthorId, setId)) {
            Optional<AccessRole> accessRole = accessRoleRepository
                .findBySet_IdAndUser_Id(setId, userId);

            accessRole.ifPresent(accessRoleRepository::delete);
        } else {
            throw new PermissionManagementAccessForbiddenException();
        }
    }
    // endregion

    // region PROPOSE permission
    @Transactional
    public void addProposePermission(Long setId, Long userId)
        throws PermissionManagementAccessForbiddenException, LearningSetNotFoundException, UserNotFoundException {
        Long requestAuthorId = AccountService.getCurrentUserId();

        if (isOwner(requestAuthorId, setId)) {
            AccessRole accessRole = accessRoleRepository.findBySet_IdAndUser_Id(setId, userId).orElse(null);

            if (accessRole == null) {
                LearningSet ls = learningSetRepository
                    .findById(setId)
                    .orElseThrow(() -> new LearningSetNotFoundException(setId));
                Account acc = accountRepository
                    .findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));

                accessRoleRepository.save(new AccessRole(Role.PROPOSER, ls, acc));
            } else if (accessRole.getRole() == Role.READER) {
                accessRole.setRole(Role.PROPOSER);
                accessRoleRepository.save(accessRole);
            }
        } else {
            throw new PermissionManagementAccessForbiddenException();
        }
    }

    @Transactional
    public void deleteProposePermission(Long setId, Long userId)
        throws PermissionManagementAccessForbiddenException, PermissionSelfManagementException {
        Long requestAuthorId = AccountService.getCurrentUserId();

        if (Objects.equals(requestAuthorId, userId)) {
            throw new PermissionSelfManagementException();
        }

        if (isOwner(requestAuthorId, setId)) {
            AccessRole accessRole = accessRoleRepository.findBySet_IdAndUser_Id(setId, userId).orElse(null);

            if (accessRole != null && accessRole.getRole().compareTo(Role.READER) > 0) {
                accessRole.setRole(Role.READER);
                accessRoleRepository.save(accessRole);
            }
        } else {
            throw new PermissionManagementAccessForbiddenException();
        }
    }
    // endregion

    // region EDIT permission
    @Transactional
    public void addEditPermission(Long setId, Long userId)
        throws UserNotFoundException, PermissionManagementAccessForbiddenException, LearningSetNotFoundException {
        Long requestAuthorId = AccountService.getCurrentUserId();

        if (isOwner(requestAuthorId, setId)) {
            Optional<AccessRole> accessRole = accessRoleRepository.findBySet_IdAndUser_Id(setId, userId);

            if (accessRole.isEmpty()) {
                LearningSet ls = learningSetRepository
                    .findById(setId)
                    .orElseThrow(() -> new LearningSetNotFoundException(setId));
                Account acc = accountRepository
                    .findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));
                accessRoleRepository.save(new AccessRole(Role.EDITOR, ls, acc));
            } else if (accessRole.get().getRole().compareTo(Role.EDITOR) < 0) {
                accessRole.map(ar -> {
                    ar.setRole(Role.EDITOR);
                    return accessRoleRepository.save(ar);
                });
            }
        } else {
            throw new PermissionManagementAccessForbiddenException();
        }
    }

    @Transactional
    public void deleteEditPermission(Long setId, Long userId)
        throws PermissionManagementAccessForbiddenException, PermissionSelfManagementException {
        Long requestAuthorId = AccountService.getCurrentUserId();

        if (Objects.equals(requestAuthorId, userId)) {
            throw new PermissionSelfManagementException();
        }

        if (isOwner(requestAuthorId, setId)) {
            Optional<AccessRole> accessRole = accessRoleRepository.findBySet_IdAndUser_Id(setId, userId);

            if (accessRole.isPresent() && accessRole.get().getRole() == Role.EDITOR) {
                accessRole.map(ar -> {
                    ar.setRole(Role.PROPOSER);
                    return accessRoleRepository.save(ar);
                });
            }
        } else {
            throw new PermissionManagementAccessForbiddenException();
        }
    }
    // endregion

    /**
     * Method verifying if given user is owner of particular learning set.
     *
     * @param userId ID of the user we want to check permissions of.
     * @param setId ID of the learning set.
     * @return true if user is owner, false otherwise.
     */
    private boolean isOwner(Long userId, Long setId) {
        return accessRoleRepository
            .findBySet_IdAndUser_Id(setId, userId)
            .map(ar -> ar.getRole() == Role.OWNER)
            .orElse(false);
    }
}
