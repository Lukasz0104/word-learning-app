package pl.lodz.p.it.wordapp.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sets")
@SecurityRequirement(name = "bearerAuth")
public class LearningSetController {

    private final LearningSetRepository repository;

    @GetMapping
    public List<LearningSetDetailsDto> all() {
        return repository
                .findAllBy();
    }

    @GetMapping("/{id}")
    public LearningSetDetailsDto one(@PathVariable Long id) {
        return repository
                .findDistinctById(id)
                .orElseThrow(() -> new LearningSetNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningSetDetailsDto create(@RequestBody CreateLearningSetDto createLearningSetDto) {
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
    public LearningSetDetailsDto update(
            @RequestBody CreateLearningSetDto dto,
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
