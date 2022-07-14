package pl.lodz.p.it.wordapp.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.AccessRole;
import pl.lodz.p.it.wordapp.model.Account;
import pl.lodz.p.it.wordapp.model.LearningSet;
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

        Long userId = getCurrentUserId();

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

    public LearningSetDetailsDto findOne(Long id) throws LearningSetAccessForbiddenException {
        Long userId = getCurrentUserId();
        AccessRole role = accessRoleRepository.findBySet_IdAndUser_Id(id, userId);

        LearningSetDetailsDto ls = learningSetRepository
                .findDistinctById(id)
                .orElseThrow(() -> new LearningSetNotFoundException(id));

        if (!ls.isPubliclyVisible() && role == null) {
            throw new LearningSetAccessForbiddenException(id);
        }

        return ls;
    }

    public LearningSetDetailsDto create(CreateLearningSetDto learningSet) {
        // TODO set current user
        LearningSet ls = CreateLearningSetDto.mapToLearningSet(learningSet);
        return new LearningSetDetailsDto(learningSetRepository.save(ls));
    }

    public LearningSetDetailsDto replace(CreateLearningSetDto learningSet, Long id) {
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

    public void delete(Long id) {
        if (learningSetRepository.existsById(id)) {
            learningSetRepository.deleteById(id);
        } else {
            throw new LearningSetNotFoundException(id);
        }
    }

    /**
     * Retrieve id of the user, that is currently logged in.
     *
     * @return user's ID if user is authenticated, otherwise null.
     */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;

        if (auth.getPrincipal() instanceof Account) {
            userId = ((Account) auth.getPrincipal()).getId();
        }
        return userId;
    }
}
