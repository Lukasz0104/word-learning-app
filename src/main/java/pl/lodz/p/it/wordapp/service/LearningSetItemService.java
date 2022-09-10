package pl.lodz.p.it.wordapp.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetItemDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetItemModificationAccessForbiddenException;
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

    public List<LearningSetItemDto> findAll(Long setId)
        throws LearningSetNotFoundException, LearningSetAccessForbiddenException {
        LearningSetDetailsDto set = setRepository.findDistinctById(setId)
                                                 .orElseThrow(() -> new LearningSetNotFoundException(setId));

        if (!set.isPubliclyVisible() && !permissionService.getPermissions(setId).isAbleToRead()) {
            throw new LearningSetAccessForbiddenException();
        }

        return itemRepository.findByLearningSetItemKey_SetID(setId);
    }

    public LearningSetItemDto findOne(Long setId, Long itemId)
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {

        LearningSetDetailsDto set = setRepository.findDistinctById(setId)
                                                 .orElseThrow(() -> new LearningSetNotFoundException(setId));

        if (!set.isPubliclyVisible() && !permissionService.getPermissions(setId).isAbleToRead()) {
            throw new LearningSetAccessForbiddenException();
        }

        return itemRepository
            .findById(new LearningSetItemKey(setId, itemId))
            .map(LearningSetItemDto::new)
            .orElseThrow(() -> new LearningSetItemNotFoundException(setId, itemId));
    }

    public LearningSetItemDto create(CreateLearningSetItemDto dto, Long setId)
        throws LearningSetNotFoundException, LearningSetItemModificationAccessForbiddenException {

        if (!permissionService.getPermissions(setId).isAbleToEdit()) {
            throw new LearningSetItemModificationAccessForbiddenException();
        }

        LearningSet ls = setRepository
            .findById(setId)
            .orElseThrow(() -> new LearningSetNotFoundException(setId));

        Long itemID = itemRepository.findNextId(setId);

        LearningSetItem lsi = CreateLearningSetItemDto.mapToLearningSetItem(dto, setId, itemID);
        lsi.setSet(ls);

        return new LearningSetItemDto(itemRepository.save(lsi));
    }

    public LearningSetItemDto replace(CreateLearningSetItemDto dto, Long setId, Long itemId)
        throws LearningSetNotFoundException, LearningSetItemNotFoundException,
               LearningSetItemModificationAccessForbiddenException {

        if (!permissionService.getPermissions(setId).isAbleToEdit()) {
            throw new LearningSetItemModificationAccessForbiddenException();
        }

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
        throws LearningSetItemNotFoundException, LearningSetNotFoundException,
               LearningSetItemModificationAccessForbiddenException {

        if (!permissionService.getPermissions(setId).isAbleToEdit()) {
            throw new LearningSetItemModificationAccessForbiddenException();
        }

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
