package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @NotBlank(message = "Term cannot be empty")
    @Size(max = 255, message = "Term must be at most 255 characters long")
    private String term;

    @Column(name = "TRANSLATION")
    @NotBlank(message = "Translation cannot be empty")
    @Size(max = 255, message = "Translation must be at most 255 characters long")
    private String translation;

    @MapsId("setID")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private LearningSet set;

    @PrePersist
    @PreUpdate
    private void trim() {
        term = term.trim();
        translation = translation.trim();
    }
}