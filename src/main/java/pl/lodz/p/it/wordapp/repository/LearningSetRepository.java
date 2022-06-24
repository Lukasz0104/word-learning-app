package pl.lodz.p.it.wordapp.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.lodz.p.it.wordapp.model.LearningSet;

public interface LearningSetRepository extends PagingAndSortingRepository<LearningSet, Long> {
}