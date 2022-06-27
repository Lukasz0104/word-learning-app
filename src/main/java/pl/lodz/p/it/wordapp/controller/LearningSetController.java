package pl.lodz.p.it.wordapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sets")
public class LearningSetController {

    private final LearningSetRepository repository;

    @GetMapping
    public Iterable<LearningSet> all() {
        return repository
                .findAll();
    }

    @GetMapping("/{id}")
    public LearningSet one(@PathVariable Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new LearningSetNotFoundException(id));
    }

    // @PostMapping
    // public LearningSet add(@RequestBody LearningSet learningSet) {
    //     // TODO create DTO
    //     return repository.save(learningSet);
    // }
}
