package pl.lodz.p.it.wordapp.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.model.LearningSet;

public interface LearningSetRepository extends JpaRepository<LearningSet, Long> {

    @Query("SELECT ls " +
        "FROM LearningSet ls " +
        "LEFT JOIN FETCH AccessRole ar " +
        "ON ls.id = ar.set.id AND ar.user.id = ?1 " +
        "WHERE ((ls.publiclyVisible = true) " +
        "OR (ls.publiclyVisible = false AND ar.roleValue IS NOT NULL)) " +
        "AND ((?2) IS NULL OR LOWER(ls.termLanguage) IN ?2) " +
        "AND ((?3) IS NULL OR LOWER(ls.translationLanguage) IN ?3) " +
        "AND (?4 IS NULL OR LOWER(ls.title) LIKE CONCAT('%', LOWER(?4), '%'))" +
        "ORDER BY ls.id ASC")
    List<LearningSetDetailsDto> find(Long userId,
                                     @Nullable Collection<String> termLanguages,
                                     @Nullable Collection<String> translationLanguages,
                                     @Nullable String titlePattern,
                                     Pageable page);

    Optional<LearningSetDetailsDto> findDistinctById(Long id);
}
