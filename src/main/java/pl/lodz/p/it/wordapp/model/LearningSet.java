package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PUBLICLY_VISIBLE")
    private boolean publiclyVisible;

    @Column(name = "CREATION_TIME")
    @JsonIgnore
    private final LocalDateTime creationTime;

    @Column(name = "TERM_LANGUAGE", length = 2)
    private String termLanguage;

    @Column(name = "TRANSLATION_LANGUGE", length = 2)
    private String translationLanguage;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    @JsonIgnore
    private Account author;

    public LearningSet() {
        this.creationTime = LocalDateTime.now();
    }
}