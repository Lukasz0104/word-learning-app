package pl.lodz.p.it.wordapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.model.LearningSetItem;
import pl.lodz.p.it.wordapp.model.LearningSetItemKey;

public interface LearningSetItemRepository
        extends JpaRepository<LearningSetItem, LearningSetItemKey> {

    List<LearningSetItemDto> findByLearningSetItemKey_SetID(Long setID);

}