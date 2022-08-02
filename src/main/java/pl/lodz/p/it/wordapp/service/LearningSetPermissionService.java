package pl.lodz.p.it.wordapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.controller.dto.PermissionsDto;
import pl.lodz.p.it.wordapp.model.Role;
import pl.lodz.p.it.wordapp.repository.AccessRoleRepository;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@Service
@RequiredArgsConstructor
public class LearningSetPermissionService {
    private final AccessRoleRepository accessRoleRepository;
    private final LearningSetRepository learningSetRepository;

    public PermissionsDto getPermissions(Long setId) {
        Long userId = UserService.getCurrentUserId();

        return accessRoleRepository
                .findBySet_IdAndUser_Id(setId, userId)
                .map(PermissionsDto::new)
                .orElseGet(() -> learningSetRepository
                        .findDistinctById(setId)
                        .filter(LearningSetDetailsDto::isPubliclyVisible)
                        .map(dto -> new PermissionsDto(Role.READER))
                        .orElseGet(PermissionsDto::new));
    }
}
