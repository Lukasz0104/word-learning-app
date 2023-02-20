package pl.lodz.p.it.wordapp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetDeletionAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;

@SpringBootTest
@Transactional
class LearningSetServiceTest {

    CreateLearningSetDto updatedLearningSet;

    @Autowired
    private LearningSetService service;

    @BeforeEach
    void setupUpdatedLearningSet() {
        updatedLearningSet = new CreateLearningSetDto();
        updatedLearningSet.setTitle("updated title");
        updatedLearningSet.setPubliclyVisible(false);
        updatedLearningSet.setTermLanguage("la");
        updatedLearningSet.setTranslationLanguage("en");
    }

    @Test
    @WithAnonymousUser
    void findOneAnonymousUserTest() throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        LearningSetDetailsDto ls1;
        ls1 = service.findOne(1L);
        assertEquals(ls1.getId(), 1L);
        assertTrue(ls1.isPubliclyVisible());

        assertThrows(LearningSetAccessForbiddenException.class, () -> service.findOne(2L));

        assertThrows(LearningSetAccessForbiddenException.class, () -> service.findOne(3L));
    }

    @Test
    @WithUserDetails("user1")
    void findOneForUser1Test() throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        LearningSetDetailsDto ls1 = service.findOne(1L);
        assertEquals(1L, ls1.getId());
        assertTrue(ls1.isPubliclyVisible());
        assertEquals("niemieckie słówka", ls1.getTitle());

        LearningSetDetailsDto ls2 = service.findOne(2L);
        assertEquals(ls2.getId(), 2L);
        assertFalse(ls2.isPubliclyVisible());
        assertEquals("animals in german", ls2.getTitle());

        LearningSetDetailsDto ls3 = service.findOne(3L);
        assertEquals(3L, ls3.getId());
        assertFalse(ls3.isPubliclyVisible());
        assertEquals("polish words", ls3.getTitle());
    }

    @Test
    @WithUserDetails("user2")
    void findOneForUser2Test() throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        LearningSetDetailsDto ls1 = service.findOne(1L);
        assertEquals(1L, ls1.getId());
        assertTrue(ls1.isPubliclyVisible());

        LearningSetDetailsDto ls2 = service.findOne(2L);
        assertEquals(2L, ls2.getId());
        assertFalse(ls2.isPubliclyVisible());

        assertThrows(LearningSetAccessForbiddenException.class, () -> service.findOne(3L));
    }

    @Test
    @WithUserDetails("user1")
    void findOneNotFoundTest() {
        assertThrows(LearningSetNotFoundException.class, () -> service.findOne(-100000L));
    }

    @Test
    @WithAnonymousUser
    void findAllAnonymousUserNoParamsTest() {
        List<LearningSetDetailsDto> learningSets = service.findAll(null, null, null, 0);
        assertEquals(1, learningSets.size());
    }

    @Test
    @WithUserDetails("user1")
    void findAllUser1NoParamsTest() {
        List<LearningSetDetailsDto> sets = service.findAll(null, null, null, 0);
        assertEquals(3, sets.size());
    }

    @Test
    @WithUserDetails("user2")
    void findAllUser2NoParamsTest() {
        List<LearningSetDetailsDto> sets = service.findAll(null, null, null, 0);
        assertEquals(2, sets.size());
    }

    @Test
    @WithUserDetails("user1")
    void findAllUser1WithTermLanguagesParamTest() {
        List<String> termLanguages = List.of("de");
        List<LearningSetDetailsDto> sets = service.findAll(termLanguages, null, null, 0);

        assertEquals(2, sets.size());
        for (LearningSetDetailsDto set : sets) {
            assertEquals(set.getTermLanguage(), "de");
        }
    }

    @Test
    @WithUserDetails("user3")
    void findAllUser3WithTranslationLanguageTest() {
        List<String> translationLanguages = List.of("en");
        List<LearningSetDetailsDto> sets = service.findAll(null, translationLanguages, null, 0);

        assertEquals(1, sets.size());
        for (LearningSetDetailsDto set : sets) {
            assertEquals(set.getTranslationLanguage(), "en");
        }
    }

    @Test
    @WithUserDetails("user4")
    void findAllUser4TitlePatternParamTest() {
        List<LearningSetDetailsDto> sets = service.findAll(null, null, "ni", 0);
        assertEquals(2, sets.size());
    }

    @Test
    @WithUserDetails("user2")
    void deleteSuccessTest() {
        assertDoesNotThrow(() -> service.delete(2L));
        assertThrows(LearningSetNotFoundException.class, () -> service.findOne(2L));
        assertThrows(LearningSetNotFoundException.class, () -> service.delete(2L));
    }

    @Test
    @WithUserDetails("user3")
    void deleteFailTest() {
        assertThrows(LearningSetDeletionAccessForbiddenException.class, () -> service.delete(2L));
    }

    @Test
    @WithUserDetails("user2")
    void replaceAsOwnerTest() throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        service.replace(updatedLearningSet, 2L);

        LearningSetDetailsDto ls = service.findOne(2L);

        assertEquals("updated title", ls.getTitle());
        assertEquals("la", ls.getTermLanguage());
        assertEquals("en", ls.getTranslationLanguage());
    }

    @Test
    @WithUserDetails("user4")
    void replaceAsEditorTest() throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        service.replace(updatedLearningSet, 2L);

        LearningSetDetailsDto ls = service.findOne(2L);

        assertEquals("updated title", ls.getTitle());
        assertEquals("la", ls.getTermLanguage());
        assertEquals("en", ls.getTranslationLanguage());
    }

    @Test
    @WithUserDetails("user3")
    void replaceAsProposerTest() {
        assertThrows(LearningSetAccessForbiddenException.class, () -> service.replace(updatedLearningSet, 2L));
    }

    @Test
    @WithUserDetails("user4")
    void createSetTest() throws LearningSetAccessForbiddenException, LearningSetNotFoundException {
        CreateLearningSetDto createLearningSetDto = new CreateLearningSetDto();
        createLearningSetDto.setTitle("new set");
        createLearningSetDto.setTranslationLanguage("es");
        createLearningSetDto.setTermLanguage("ru");
        createLearningSetDto.setPubliclyVisible(false);

        LearningSetDetailsDto created = service.create(createLearningSetDto);
        Long id = created.getId();

        LearningSetDetailsDto retrieved = service.findOne(id);
        assertNotNull(retrieved);
    }
}
