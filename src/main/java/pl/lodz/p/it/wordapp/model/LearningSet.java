package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "learning_set")
@Getter
@Setter
@ToString
public class LearningSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    private boolean publiclyVisible;

    private LocalDateTime creationTime;

    private String termLanguage;

    private String translationLanguage;

    @PrePersist
    public void prePersist() {
        creationTime = LocalDateTime.now();
    }
}