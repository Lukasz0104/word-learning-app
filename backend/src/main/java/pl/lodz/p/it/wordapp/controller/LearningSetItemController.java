package pl.lodz.p.it.wordapp.controller;

import javax.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetItemDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetItemModificationAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetItemNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.service.LearningSetItemService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sets/{setID}/items")
@SecurityRequirement(name = "bearerAuth")
public class LearningSetItemController {

    private final LearningSetItemService itemService;

    @GetMapping
    public List<LearningSetItemDto> all(@PathVariable Long setID)
        throws LearningSetNotFoundException, LearningSetAccessForbiddenException {
        return itemService.findAll(setID);
    }

    @GetMapping("/{itemID}")
    public LearningSetItemDto one(@PathVariable Long setID,
                                  @PathVariable Long itemID)
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {
        return itemService.findOne(setID, itemID);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningSetItemDto create(@Valid @RequestBody CreateLearningSetItemDto dto,
                                     @PathVariable Long setID)
        throws LearningSetNotFoundException, LearningSetItemModificationAccessForbiddenException {
        return itemService.create(dto, setID);
    }

    @PutMapping("/{itemID}")
    public LearningSetItemDto replace(@Valid @RequestBody CreateLearningSetItemDto dto,
                                      @PathVariable Long setID,
                                      @PathVariable Long itemID)
        throws LearningSetNotFoundException, LearningSetItemNotFoundException,
               LearningSetItemModificationAccessForbiddenException {
        return itemService.replace(dto, setID, itemID);
    }

    @DeleteMapping("/{itemID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long setID,
                       @PathVariable Long itemID)
        throws LearningSetItemNotFoundException, LearningSetNotFoundException,
               LearningSetItemModificationAccessForbiddenException {
        itemService.delete(setID, itemID);
    }
}
