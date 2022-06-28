package pl.lodz.p.it.wordapp.controller.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.wordapp.model.LearningSet;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LearningSetDto implements Serializable {
    private Long id;
    private boolean publiclyVisible;
    private String termLanguage;
    private String translationLanguage;

    public LearningSetDto(LearningSet learningSet) {
        this(learningSet.getId(), learningSet.isPubliclyVisible(),
                learningSet.getTermLanguage(), learningSet.getTranslationLanguage());
    }
}
