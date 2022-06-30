package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;

@Entity
@Table(name = "LEARNING_SET_ITEM")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LearningSetItem {

    @EmbeddedId
    private LearningSetItemKey learningSetItemKey;

    @Column(name = "TERM")
    private String term;

    @Column(name = "TRANSLATION")
    private String translation;

    @MapsId("setID")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private LearningSet set;

    public LearningSetItem(LearningSetItemDto dto, Long itemID) {
        this.learningSetItemKey = new LearningSetItemKey(dto.getLearningSetItemKeySetID(), itemID);
        this.term = dto.getTerm();
        this.translation = dto.getTranslation();
    }

}