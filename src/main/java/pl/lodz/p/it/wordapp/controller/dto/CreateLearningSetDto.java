package pl.lodz.p.it.wordapp.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import pl.lodz.p.it.wordapp.model.LearningSet;

@NoArgsConstructor
@Setter
@Getter
public class CreateLearningSetDto {

    @NotBlank(message = "Title cannot be empty")
    @Length(max = 200, message = "Title must be at most 200 characters long")
    private String title;

    private boolean publiclyVisible;

    @NotBlank(message = "Term language cannot be empty")
    @Pattern(regexp = "^\s*[a-z]{2}\s*$", message = "Term language must be a 2 letter language code")
    private String termLanguage;

    @NotBlank(message = "Translation language cannot be empty")
    @Pattern(regexp = "^\s*[a-z]{2}\s*$", message = "Translation language must be a 2 letter language code")
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
