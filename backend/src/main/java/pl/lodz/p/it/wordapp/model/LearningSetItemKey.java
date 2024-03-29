package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class LearningSetItemKey implements Serializable {

    @Column(name = "SET_ID", insertable = false, updatable = false)
    private Long setID;

    @Column(name = "ITEM_ID")
    private Long itemID;

    public LearningSetItemKey(Long setID, Long itemID) {
        this.setID = setID;
        this.itemID = itemID;
    }
}
