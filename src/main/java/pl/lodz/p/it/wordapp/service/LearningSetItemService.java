package pl.lodz.p.it.wordapp.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetItemDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.exception.LearningSetItemNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.model.LearningSetItem;
import pl.lodz.p.it.wordapp.model.LearningSetItemKey;
import pl.lodz.p.it.wordapp.repository.LearningSetItemRepository;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@Service
@RequiredArgsConstructor
public class LearningSetItemService {
    private final LearningSetItemRepository itemRepository;
    private final LearningSetRepository setRepository;
    private final LearningSetPermissionService permissionService;

    public List<LearningSetItemDto> findAll(Long setId) throws LearningSetNotFoundException {
        // TODO check role
        if (!setRepository.existsById(setId)) {
            throw new LearningSetNotFoundException(setId);
        }

        return itemRepository.findByLearningSetItemKey_SetID(setId);
    }

    public LearningSetItemDto findOne(Long setId, Long itemId)
        throws LearningSetNotFoundException, LearningSetItemNotFoundException {
        // TODO check role
        if (!setRepository.existsById(setId)) {
            throw new LearningSetNotFoundException(setId);
        }

        LearningSetItem item = itemRepository
            .findById(new LearningSetItemKey(setId, itemId))
            .orElseThrow(() -> new LearningSetItemNotFoundException(setId, itemId));

        return new LearningSetItemDto(item);
    }

    public LearningSetItemDto create(CreateLearningSetItemDto dto, Long setId)
        throws LearningSetNotFoundException {
        // TODO check role
        LearningSet ls = setRepository
            .findById(setId)
            .orElseThrow(() -> new LearningSetNotFoundException(setId));

        Long itemID = itemRepository.findNextId(setId);

        LearningSetItem lsi = CreateLearningSetItemDto.mapToLearningSetItem(dto, setId, itemID);
        lsi.setSet(ls);

        LearningSetItem created = itemRepository.save(lsi);
        return new LearningSetItemDto(created);
    }

    public LearningSetItemDto replace(CreateLearningSetItemDto dto, Long setId, Long itemId)
        throws LearningSetNotFoundException, LearningSetItemNotFoundException {
        // TODO check role
        if (!setRepository.existsById(setId)) {
            throw new LearningSetNotFoundException(setId);
        }

        LearningSetItem updated = itemRepository
            .findById(new LearningSetItemKey(setId, itemId))
            .map(lsi -> {
                lsi.setTerm(dto.getTerm());
                lsi.setTranslation(dto.getTranslation());

                return itemRepository.save(lsi);
            })
            .orElseThrow(() -> new LearningSetItemNotFoundException(setId, itemId));

        return new LearningSetItemDto(updated);
    }

    public void delete(Long setId, Long itemId)
        // TODO check role
        throws LearningSetItemNotFoundException, LearningSetNotFoundException {
        if (!setRepository.existsById(setId)) {
            throw new LearningSetNotFoundException(setId);
        }

        LearningSetItemKey id = new LearningSetItemKey(setId, itemId);

        if (!itemRepository.existsById(id)) {
            throw new LearningSetItemNotFoundException(id);
        }

        itemRepository.deleteById(id);
    }
}
