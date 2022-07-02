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
import lombok.Builder;
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
@Builder
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
}