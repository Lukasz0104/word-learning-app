package pl.lodz.p.it.wordapp.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.exception.LearningSetItemNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.LearningSetItem;
import pl.lodz.p.it.wordapp.model.LearningSetItemKey;
import pl.lodz.p.it.wordapp.repository.LearningSetItemRepository;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sets/{setID}/items")
public class LearningSetItemController {

    private final LearningSetRepository setRepository;
    private final LearningSetItemRepository itemRepository;

    @GetMapping
    public List<LearningSetItemDto> all(@PathVariable Long setID)
            throws LearningSetNotFoundException {

        if (!setRepository.existsById(setID)) {
            throw new LearningSetNotFoundException(setID);
        }

        return itemRepository.findByLearningSetItemKey_SetID(setID);
    }

    @GetMapping("/{itemID}")
    public LearningSetItemDto one(
            @PathVariable Long setID,
            @PathVariable Long itemID) throws LearningSetNotFoundException {

        if (!setRepository.existsById(setID)) {
            throw new LearningSetNotFoundException(setID);
        }

        LearningSetItem item = itemRepository
                .findById(new LearningSetItemKey(setID, itemID))
                .orElseThrow(() -> new LearningSetItemNotFoundException(setID, itemID));

        return new LearningSetItemDto(item);
    }
}
