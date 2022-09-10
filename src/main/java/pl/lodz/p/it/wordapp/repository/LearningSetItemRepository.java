package pl.lodz.p.it.wordapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.model.LearningSetItem;
import pl.lodz.p.it.wordapp.model.LearningSetItemKey;

public interface LearningSetItemRepository
    extends JpaRepository<LearningSetItem, LearningSetItemKey> {

    List<LearningSetItemDto> findByLearningSetItemKey_SetID(Long setID);

    @Query("SELECT (1 + COALESCE(MAX(l.learningSetItemKey.itemID), 0)) " +
        "FROM LearningSetItem l " +
        "WHERE l.learningSetItemKey.setID = ?1")
    Long findNextId(Long setID);

}
