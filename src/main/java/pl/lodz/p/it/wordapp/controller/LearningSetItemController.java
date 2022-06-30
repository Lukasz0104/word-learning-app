package pl.lodz.p.it.wordapp.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.exception.LearningSetItemNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.LearningSet;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningSetItemDto create(
            @RequestBody LearningSetItemDto dto,
            @PathVariable Long setID) {

        LearningSet ls = setRepository
                .findById(setID)
                .orElseThrow(() -> new LearningSetNotFoundException(setID));

        Long itemID = itemRepository.findNextId(setID);

        dto.setLearningSetItemKeySetID(setID);
        LearningSetItem lsi = new LearningSetItem(dto, itemID);
        lsi.setSet(ls);

        LearningSetItem created = itemRepository.save(lsi);
        return new LearningSetItemDto(created);
    }
}
