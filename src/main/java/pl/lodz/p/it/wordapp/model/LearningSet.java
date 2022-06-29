package pl.lodz.p.it.wordapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDto;

@Entity
@Table(name = "LEARNING_SET")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class LearningSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "PUBLICLY_VISIBLE")
    private boolean publiclyVisible;

    @Column(name = "CREATION_TIME")
    @JsonIgnore
    private final LocalDateTime creationTime;

    @Column(name = "TERM_LANGUAGE", length = 2)
    private String termLanguage;

    @Column(name = "TRANSLATION_LANGUGE", length = 2)
    private String translationLanguage;

    // @ManyToOne
    // @JoinColumn(name = "AUTHOR_ID", updatable = false, insertable = false)
    // private Account author;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "SET_ID", updatable = false, insertable = false)
    private List<LearningSetItem> items;

    public LearningSet() {
        this.creationTime = LocalDateTime.now();
    }

    public LearningSet(LearningSetDto learningSetDto) {
        this.publiclyVisible = learningSetDto.isPubliclyVisible();
        this.termLanguage = learningSetDto.getTermLanguage();
        this.translationLanguage = learningSetDto.getTranslationLanguage();
        this.title = learningSetDto.getTitle();
        this.creationTime = LocalDateTime.now();
    }
}