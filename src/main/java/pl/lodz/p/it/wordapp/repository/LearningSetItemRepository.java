package pl.lodz.p.it.wordapp.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.lodz.p.it.wordapp.model.LearningSetItem;
import pl.lodz.p.it.wordapp.model.LearningSetItemKey;

public interface LearningSetItemRepository
        extends PagingAndSortingRepository<LearningSetItem, LearningSetItemKey> {
}