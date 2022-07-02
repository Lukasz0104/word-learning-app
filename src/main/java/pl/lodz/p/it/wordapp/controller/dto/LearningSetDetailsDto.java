package pl.lodz.p.it.wordapp.controller.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.wordapp.model.LearningSet;

@Getter
@NoArgsConstructor
public class LearningSetDetailsDto implements Serializable {
    private Long id;
    private String title;
    private boolean publiclyVisible;
    private String termLanguage;
    private String translationLanguage;

    public LearningSetDetailsDto(LearningSet learningSet) {
        this.id = learningSet.getId();
        this.title = learningSet.getTitle();
        this.publiclyVisible = learningSet.isPubliclyVisible();
        this.termLanguage = learningSet.getTermLanguage();
        this.translationLanguage = learningSet.getTranslationLanguage();
    }

    public static LearningSet mapToLearningSet(LearningSetDetailsDto dto) {
        return LearningSet.builder()
                .id(dto.id)
                .title(dto.title)
                .publiclyVisible(dto.publiclyVisible)
                .termLanguage(dto.termLanguage)
                .translationLanguage(dto.translationLanguage)
                .build();
    }
}
