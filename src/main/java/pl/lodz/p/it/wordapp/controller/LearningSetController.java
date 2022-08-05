package pl.lodz.p.it.wordapp.controller;

import javax.validation.Valid;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetDeletionAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.service.LearningSetService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sets")
@SecurityRequirement(name = "bearerAuth")
public class LearningSetController {

    private final LearningSetService service;

    @GetMapping
    public List<LearningSetDetailsDto> all(
        @RequestParam(name = "termLanguages", required = false) Collection<String> termLanguages,
        @RequestParam(name = "translationLanguages", required = false) Collection<String> translationLanguages,
        @RequestParam(name = "titlePattern", required = false) String titlePattern) {

        return service.findAll(termLanguages, translationLanguages, titlePattern);
    }

    @GetMapping("/{id}")
    public LearningSetDetailsDto one(@PathVariable Long id)
        throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        return service.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningSetDetailsDto create(@Valid @RequestBody CreateLearningSetDto createLearningSetDto) {
        return service.create(createLearningSetDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id)
        throws LearningSetNotFoundException, LearningSetDeletionAccessForbiddenException {
        service.delete(id);
    }

    @PutMapping("/{id}")
    public LearningSetDetailsDto replace(@Valid @RequestBody CreateLearningSetDto dto,
                                         @PathVariable Long id)
        throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        return service.replace(dto, id);
    }
}
