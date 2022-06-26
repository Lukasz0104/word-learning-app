package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "LEARNING_SET")
@Getter
@Setter
@ToString
public class LearningSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private boolean publiclyVisible;

    private final LocalDateTime creationTime;

    @Column(length = 2)
    private String termLanguage;

    @Column(length = 2)
    private String translationLanguage;

    public LearningSet() {
        this.creationTime = LocalDateTime.now();
    }

    public LearningSet(boolean publiclyVisible, String termLanguage, String translationLanguage) {
        this.publiclyVisible = publiclyVisible;
        this.termLanguage = termLanguage;
        this.translationLanguage = translationLanguage;
        this.creationTime = LocalDateTime.now();
    }
}