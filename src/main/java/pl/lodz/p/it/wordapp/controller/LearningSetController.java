package pl.lodz.p.it.wordapp.controller;

import javax.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.Account;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sets")
@SecurityRequirement(name = "bearerAuth")
public class LearningSetController {

    private final LearningSetRepository repository;

    @GetMapping
    public List<LearningSetDetailsDto> all(
            @RequestParam(name = "termLanguages", required = false) List<String> termLanguages,
            @RequestParam(name = "translationLanguages", required = false) List<String> translationLanguages) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;

        if (auth.getPrincipal() instanceof Account) {
            userId = ((Account) auth.getPrincipal()).getId();
        }

        if (termLanguages != null) {
            termLanguages = termLanguages
                    .stream()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> s.matches("^[a-z]{2}$"))
                    .toList();
        }

        if (translationLanguages != null) {
            translationLanguages = translationLanguages
                    .stream()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> s.matches("^[a-z]{2}$"))
                    .toList();
        }

        return repository.find(userId, termLanguages, translationLanguages);

        // if (termLanguages != null && termLanguages.size() > 0) {
        //     if (translationLanguages != null && translationLanguages.size() > 0) {
        //         return repository
        //                 .findSetsByTermLanguageInAndTranslationLanguageIn(
        //                         userId, termLanguages, translationLanguages);
        //     } else {
        //         return repository
        //                 .findByTermLanguageIn(userId, termLanguages);
        //     }
        // } else {
        //     if (translationLanguages != null && translationLanguages.size() > 0) {
        //         return repository
        //                 .findByTranslationLanguageIn(userId, translationLanguages);
        //     }
        //     return repository.findSets(userId);
        // }
    }

    @GetMapping("/{id}")
    public LearningSetDetailsDto one(@PathVariable Long id) {
        return repository
                .findDistinctById(id)
                .orElseThrow(() -> new LearningSetNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningSetDetailsDto create(
            @Valid @RequestBody CreateLearningSetDto createLearningSetDto) {
        LearningSet ls = CreateLearningSetDto.mapToLearningSet(createLearningSetDto);
        // TODO set current user
        return new LearningSetDetailsDto(repository.save(ls));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new LearningSetNotFoundException(id);
        }
    }

    @PutMapping("/{id}")
    public LearningSetDetailsDto replace(
            @Valid @RequestBody CreateLearningSetDto dto,
            @PathVariable Long id) {

        LearningSet updated = repository
                .findById(id)
                .map(ls -> {
                    ls.setPubliclyVisible(dto.isPubliclyVisible());
                    ls.setTitle(dto.getTitle());
                    ls.setTermLanguage(dto.getTermLanguage());
                    ls.setTranslationLanguage(dto.getTranslationLanguage());

                    return repository.save(ls);
                })
                .orElseThrow(() -> new LearningSetNotFoundException(id));

        return new LearningSetDetailsDto(updated);
    }
}
