package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "learning_set")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LearningSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    private boolean publiclyVisible;

    private LocalDateTime creationTime;

    @Column(length = 2)
    private String termLanguage;

    @Column(length = 2)
    private String translationLanguage;

    public LearningSet(boolean publiclyVisible, String termLanguage, String translationLanguage) {
        this.publiclyVisible = publiclyVisible;
        this.termLanguage = termLanguage;
        this.translationLanguage = translationLanguage;
        this.creationTime = LocalDateTime.now();
    }
}