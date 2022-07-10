package pl.lodz.p.it.wordapp.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.wordapp.model.LearningSetItem;
import pl.lodz.p.it.wordapp.model.LearningSetItemKey;

@NoArgsConstructor
@Getter
@Setter
public class CreateLearningSetItemDto {
    @NotBlank(message = "Term cannot be empty")
    @Size(max = 255, message = "Term must be at most 255 characters long")
    private String term;

    @NotBlank(message = "Translation cannot be empty")
    @Size(max = 255, message = "Translation must be at most 255 characters long")
    private String translation;

    public static LearningSetItem mapToLearningSetItem(CreateLearningSetItemDto dto,
                                                       Long setID,
                                                       Long itemID) {

        LearningSetItemKey key = new LearningSetItemKey(setID, itemID);
        return LearningSetItem.builder()
                .learningSetItemKey(key)
                .term(dto.term)
                .translation(dto.translation)
                .build();
    }
}
