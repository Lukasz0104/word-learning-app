package pl.lodz.p.it.wordapp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.wordapp.model.LearningSet;

@NoArgsConstructor
@Setter
@Getter
public class CreateLearningSetDto {
    private String title;
    private boolean publiclyVisible;
    private String termLanguage;
    private String translationLanguage;

    public static LearningSet mapToLearningSet(CreateLearningSetDto dto) {
        return LearningSet.builder()
                .translationLanguage(dto.translationLanguage)
                .title(dto.title)
                .termLanguage(dto.termLanguage)
                .publiclyVisible(dto.publiclyVisible)
                .build();
    }
}
