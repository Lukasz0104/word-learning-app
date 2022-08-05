package pl.lodz.p.it.wordapp.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetDeletionAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.AccessRole;
import pl.lodz.p.it.wordapp.model.Account;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.model.Role;
import pl.lodz.p.it.wordapp.repository.AccessRoleRepository;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@Service
@RequiredArgsConstructor
public class LearningSetService {

    private final LearningSetRepository learningSetRepository;
    private final AccessRoleRepository accessRoleRepository;

    public List<LearningSetDetailsDto> findAll(
        Collection<String> termLanguages,
        Collection<String> translationLanguages,
        String titlePattern) {

        Long userId = UserService.getCurrentUserId();

        if (termLanguages != null) {
            termLanguages = termLanguages
                .stream()
                .map(s -> s.trim().toLowerCase())
                .filter(s -> s.matches("^[a-z]{2}$"))
                .collect(Collectors.toSet());

            if (termLanguages.size() == 0) {
                termLanguages = null;
            }
        }

        if (translationLanguages != null) {
            translationLanguages = translationLanguages
                .stream()
                .map(s -> s.trim().toLowerCase())
                .filter(s -> s.matches("^[a-z]{2}$"))
                .collect(Collectors.toSet());

            if (translationLanguages.size() == 0) {
                translationLanguages = null;
            }
        }

        return learningSetRepository.find(userId, termLanguages, translationLanguages, titlePattern);
    }

    public LearningSetDetailsDto findOne(Long id)
        throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        Long userId = UserService.getCurrentUserId();
        Optional<AccessRole> role = accessRoleRepository.findBySet_IdAndUser_Id(id, userId);

        LearningSetDetailsDto ls = learningSetRepository
            .findDistinctById(id)
            .orElseThrow(() -> new LearningSetNotFoundException(id));

        if (!ls.isPubliclyVisible() && role.isEmpty()) {
            throw new LearningSetAccessForbiddenException(id);
        }

        return ls;
    }

    public LearningSetDetailsDto create(CreateLearningSetDto learningSet) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LearningSet persisted = learningSetRepository.save(CreateLearningSetDto.mapToLearningSet(learningSet));

        AccessRole accessRole = new AccessRole(Role.OWNER, persisted, account);
        accessRoleRepository.save(accessRole);
        return new LearningSetDetailsDto(persisted);
    }

    public LearningSetDetailsDto replace(CreateLearningSetDto learningSet, Long id)
        throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        Role role = getUserRoleForSet(id)
            .orElseThrow(() -> new LearningSetAccessForbiddenException(id));

        if (role.compareTo(Role.EDITOR) < 0) { // at least Role.EDITOR is required to make changes
            throw new LearningSetAccessForbiddenException(id);
        }

        LearningSet updated = learningSetRepository
            .findById(id)
            .map(ls -> {
                ls.setPubliclyVisible(learningSet.isPubliclyVisible());
                ls.setTitle(learningSet.getTitle());
                ls.setTermLanguage(learningSet.getTermLanguage());
                ls.setTranslationLanguage(learningSet.getTranslationLanguage());

                return learningSetRepository.save(ls);
            })
            .orElseThrow(() -> new LearningSetNotFoundException(id));

        return new LearningSetDetailsDto(updated);
    }

    public void delete(Long id) throws LearningSetDeletionAccessForbiddenException, LearningSetNotFoundException {
        if (learningSetRepository.existsById(id)) {
            Role role = getUserRoleForSet(id)
                .orElseThrow(() -> new LearningSetDeletionAccessForbiddenException(id));

            if (role == Role.OWNER) { // only author can delete
                learningSetRepository.deleteById(id);
            } else {
                throw new LearningSetDeletionAccessForbiddenException(id);
            }
        } else {
            throw new LearningSetNotFoundException(id);
        }
    }

    private Optional<Role> getUserRoleForSet(Long setId) {
        Long userId = UserService.getCurrentUserId();
        return accessRoleRepository
            .findBySet_IdAndUser_Id(setId, userId)
            .map(AccessRole::getRole);
    }
}