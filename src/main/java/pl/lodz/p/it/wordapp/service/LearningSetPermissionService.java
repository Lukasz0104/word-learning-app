package pl.lodz.p.it.wordapp.service;

import static pl.lodz.p.it.wordapp.service.UserService.getCurrentUserId;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.controller.dto.PermissionsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
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
        Long userId = getCurrentUserId();

        return accessRoleRepository
                .findBySet_IdAndUser_Id(setId, userId)
                .map(PermissionsDto::new)
                .orElseGet(() -> learningSetRepository
                        .findDistinctById(setId)
                        .filter(LearningSetDetailsDto::isPubliclyVisible)
                        .map(dto -> new PermissionsDto(Role.READER))
                        .orElseGet(PermissionsDto::new));
    }

    // region READ permission

    @Transactional
    public void addReadPermission(Long setId, Long userId) {
        Long requestAuthorId = getCurrentUserId();
        if (isOwner(requestAuthorId, setId)) {
            AccessRole accessRole = accessRoleRepository
                    .findBySet_IdAndUser_Id(setId, userId)
                    .orElse(null);

            if (accessRole == null) {
                LearningSet ls = learningSetRepository
                        .findById(setId)
                        .orElseThrow(() -> new LearningSetNotFoundException(setId));
                Account acc = accountRepository
                        .findById(userId)
                        .orElseThrow(); // TODO create custom exception

                accessRole = new AccessRole(Role.READER, ls, acc);
                accessRoleRepository.save(accessRole);
            }
        } else {
            throw new LearningSetAccessForbiddenException(setId); // TODO create specific exception
        }
    }

    @Transactional
    public void deleteAddPermission(Long setId, Long userId) {
        Long requestAuthorId = getCurrentUserId();

        if (isOwner(requestAuthorId, setId)) {

            if (Objects.equals(requestAuthorId, userId)) {
                return;
                // TODO throw custom exception
            }

            Optional<AccessRole> accessRole = accessRoleRepository
                    .findBySet_IdAndUser_Id(setId, userId);

            accessRole.ifPresent(accessRoleRepository::delete);
        } else {
            throw new LearningSetAccessForbiddenException(setId);
        }
    }

    // endregion

    /**
     * Method veryfing if given user is owner of particular learning set.
     *
     * @param userId ID of the user we want to check permissions of.
     * @param setId  ID of the learning set.
     * @return true if user is owner, false otherwise.
     */
    private boolean isOwner(Long userId, Long setId) {
        return accessRoleRepository
                .findBySet_IdAndUser_Id(setId, userId)
                .map(ar -> ar.getRole() == Role.OWNER)
                .orElse(false);
    }
}
