package pl.lodz.p.it.wordapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@RequiredArgsConstructor
@RestController
public class LearningSetController {

    private final LearningSetRepository repository;

    @GetMapping("/sets")
    public Iterable<LearningSet> getAll() {
        return repository.findAll();
    }
}
