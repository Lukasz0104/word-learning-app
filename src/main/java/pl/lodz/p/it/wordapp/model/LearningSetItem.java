package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @ManyToOne
    private LearningSet set;

}