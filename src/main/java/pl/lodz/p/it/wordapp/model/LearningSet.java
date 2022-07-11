package pl.lodz.p.it.wordapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "LEARNING_SET")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LearningSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    @NotBlank(message = "Title cannot be empty")
    @Length(max = 200, message = "Title must be at most 200 characters long")
    private String title;

    @Column(name = "PUBLICLY_VISIBLE")
    private boolean publiclyVisible;

    @Column(name = "CREATION_TIME")
    @JsonIgnore
    private final LocalDateTime creationTime = LocalDateTime.now();

    @Column(name = "TERM_LANGUAGE")
    @NotBlank(message = "Term language cannot be empty")
    @Pattern(regexp = "^\s*[a-z]{2}\s*$", message = "Term language must be a 2 letter language code")
    private String termLanguage;

    @Column(name = "TRANSLATION_LANGUAGE")
    @NotBlank(message = "Translation langugae cannot be empty")
    @Pattern(regexp = "^\s*[a-z]{2}\s*$", message = "Translation language must be a 2 letter language code")
    private String translationLanguage;

    @Formula("(SELECT COUNT(*) FROM LEARNING_SET_ITEM LSI WHERE LSI.SET_ID = id)")
    private int itemCount;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "SET_ID", updatable = false, insertable = false)
    private List<LearningSetItem> items;

    @PrePersist
    @PreUpdate
    private void trim() {
        title = title.trim();
        termLanguage = termLanguage.trim().toLowerCase();
        translationLanguage = translationLanguage.trim().toLowerCase();
    }
}