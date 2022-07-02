package pl.lodz.p.it.wordapp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.wordapp.model.LearningSetItem;
import pl.lodz.p.it.wordapp.model.LearningSetItemKey;

@NoArgsConstructor
@Getter
@Setter
public class CreateLearningSetItemDto {
    private String term;
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
