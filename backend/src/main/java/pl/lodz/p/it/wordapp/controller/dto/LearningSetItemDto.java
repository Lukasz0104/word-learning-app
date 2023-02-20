package pl.lodz.p.it.wordapp.controller.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.wordapp.model.LearningSetItem;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({ "setID", "itemID", "term", "translation" })
public class LearningSetItemDto implements Serializable {

    @JsonProperty("setID")
    private Long learningSetItemKeySetID;

    @JsonProperty("itemID")
    private Long learningSetItemKeyItemID;

    private String term;

    private String translation;

    public LearningSetItemDto(LearningSetItem item) {
        this.learningSetItemKeySetID = item.getLearningSetItemKey().getSetID();
        this.learningSetItemKeyItemID = item.getLearningSetItemKey().getItemID();
        this.term = item.getTerm();
        this.translation = item.getTranslation();
    }
}
