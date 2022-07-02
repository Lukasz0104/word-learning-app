package pl.lodz.p.it.wordapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.model.LearningSet;

public interface LearningSetRepository extends JpaRepository<LearningSet, Long> {

    List<LearningSetDetailsDto> findAllBy();

    Optional<LearningSetDetailsDto> findDistinctById(Long id);
}