package pl.lodz.p.it.wordapp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import pl.lodz.p.it.wordapp.controller.dto.CreateLearningSetItemDto;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetItemModificationAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetItemNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;

@SpringBootTest
@Transactional
@DirtiesContext
class LearningSetItemServiceTest {

    private List<LearningSetItemDto> items;
    private CreateLearningSetItemDto createLearningSetItemDto;

    @Autowired
    private LearningSetItemService itemService;

    @BeforeEach
    void setup() {
        createLearningSetItemDto = new CreateLearningSetItemDto();
        createLearningSetItemDto.setTerm("term");
        createLearningSetItemDto.setTranslation("translation");
    }

    // region findAll
    @Test
    @WithUserDetails("user4")
    void findAllPublicSetSuccessTest() throws LearningSetNotFoundException, LearningSetAccessForbiddenException {
        items = itemService.findAll(1L);

        assertNotNull(items);
        assertEquals(4, items.size());

        assertAll(() -> {
            for (var item : items) {
                assertNotNull(item);
            }
        });

        assertEquals("ja", items.get(2).getTerm());
        assertEquals("tak", items.get(2).getTranslation());
    }

    @Test
    @WithAnonymousUser
    void findAllPublicSetAnonymousUserTest() throws LearningSetNotFoundException, LearningSetAccessForbiddenException {
        List<LearningSetItemDto> items = itemService.findAll(1L);

        assertNotNull(items);
        assertEquals(4, items.size());

        assertAll(() -> {
            for (var item : items) {
                assertNotNull(item);
            }
        });

        assertEquals("nein", items.get(3).getTerm());
        assertEquals("nie", items.get(3).getTranslation());
    }

    @Test
    @WithUserDetails("user2")
    void findAllPrivateSetAsOwnerSuccessTest() throws LearningSetNotFoundException,
                                                      LearningSetAccessForbiddenException {
        items = itemService.findAll(2L);

        assertNotNull(items);
        assertEquals(3, items.size());
        assertAll(() -> {
            for (var item : items) {
                assertNotNull(item);
            }
        });

        assertEquals("e Katze", items.get(0).getTerm());
        assertEquals("cat", items.get(0).getTranslation());
    }

    @Test
    @WithUserDetails("user1")
    void findAllPrivateSetAsEditorSuccessTest() throws LearningSetNotFoundException,
                                                       LearningSetAccessForbiddenException {
        items = itemService.findAll(2L);

        assertNotNull(items);
        assertEquals(3, items.size());
        assertAll(() -> {
            for (var item : items) {
                assertNotNull(item);
            }
        });

        assertEquals("e Katze", items.get(0).getTerm());
        assertEquals("cat", items.get(0).getTranslation());
    }

    @Test
    @WithUserDetails("user4")
    void findAllPrivateSetAsProposerSuccessTest()
        throws LearningSetNotFoundException, LearningSetAccessForbiddenException {
        items = itemService.findAll(3L);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertAll(() -> {
            for (var item : items) {
                assertNotNull(item);
            }
        });

        assertEquals("tak", items.get(0).getTerm());
        assertEquals("yes", items.get(0).getTranslation());
    }

    @Test
    @WithUserDetails("user1")
    void findAllPrivateSetAsReaderSuccessTest() throws LearningSetNotFoundException,
                                                       LearningSetAccessForbiddenException {
        items = itemService.findAll(3L);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertAll(() -> {
            for (var item : items) {
                assertNotNull(item);
            }
        });

        assertEquals("tak", items.get(0).getTerm());
        assertEquals("yes", items.get(0).getTranslation());
    }

    @Test
    @WithUserDetails("user3")
    void findAllPrivateSetFailTest() {
        assertThrows(LearningSetAccessForbiddenException.class,
                     () -> itemService.findAll(2L)
        );
    }
    // endregion

    // region findOne
    @Test
    @WithUserDetails("user4")
    void findOnePublicSetSuccessTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {
        LearningSetItemDto item = itemService.findOne(1L, 3L);

        assertNotNull(item);
        assertEquals("ja", item.getTerm());
        assertEquals("tak", item.getTranslation());
    }

    @Test
    @WithAnonymousUser
    void findOnePublicSetAnonymousUserTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {
        LearningSetItemDto item = itemService.findOne(1L, 3L);

        assertNotNull(item);
        assertEquals("ja", item.getTerm());
        assertEquals("tak", item.getTranslation());
    }

    @Test
    @WithUserDetails("user2")
    void findOnePrivateSetAsOwnerTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {
        LearningSetItemDto item = itemService.findOne(2L, 3L);

        assertNotNull(item);
        assertEquals("e Kuhe", item.getTerm());
        assertEquals("cow", item.getTranslation());
    }

    @Test
    @WithUserDetails("user1")
    void findOnePrivateSetAsEditorTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {
        LearningSetItemDto item = itemService.findOne(2L, 3L);

        assertNotNull(item);
        assertEquals("e Kuhe", item.getTerm());
        assertEquals("cow", item.getTranslation());
    }

    @Test
    @WithUserDetails("user4")
    void findOnePrivateSetAsProposerTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {
        LearningSetItemDto item = itemService.findOne(3L, 2L);

        assertNotNull(item);
        assertEquals("nie", item.getTerm());
        assertEquals("no", item.getTranslation());
    }

    @Test
    @WithUserDetails("user1")
    void findOnePrivateSetAsReaderTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException {
        LearningSetItemDto item = itemService.findOne(3L, 2L);

        assertNotNull(item);
        assertEquals("nie", item.getTerm());
        assertEquals("no", item.getTranslation());
    }

    @Test
    @WithUserDetails("user3")
    void findOnePrivateSetFailTest() {
        assertThrows(LearningSetAccessForbiddenException.class,
                     () -> itemService.findOne(2L, 1L)
        );
    }

    @Test
    @WithAnonymousUser
    void findOnePrivateSetAnonymousUserTest() {
        assertThrows(LearningSetAccessForbiddenException.class,
                     () -> itemService.findOne(2L, 1L)
        );
    }
    // endregion

    // region create
    @Test
    @WithUserDetails("user1")
    void createAsOwnerTest() throws LearningSetNotFoundException, LearningSetItemModificationAccessForbiddenException {
        LearningSetItemDto created = itemService.create(createLearningSetItemDto, 1L);

        assertNotNull(created);
        assertEquals(1L, created.getLearningSetItemKeySetID());
        assertEquals(5L, created.getLearningSetItemKeyItemID());
        assertEquals("term", created.getTerm());
        assertEquals("translation", created.getTranslation());
    }

    @Test
    @WithUserDetails("user2")
    void createAsEditorTest() throws LearningSetNotFoundException, LearningSetItemModificationAccessForbiddenException {
        LearningSetItemDto created = itemService.create(createLearningSetItemDto, 1L);

        assertNotNull(created);
        assertEquals(1L, created.getLearningSetItemKeySetID());
        assertEquals(5L, created.getLearningSetItemKeyItemID());
        assertEquals("term", created.getTerm());
        assertEquals("translation", created.getTranslation());
    }

    @Test
    @WithUserDetails("user3")
    void createAsProposerTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.create(createLearningSetItemDto, 1L)
        );
    }

    @Test
    @WithUserDetails("user4")
    void createAsReaderTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.create(createLearningSetItemDto, 1L)
        );
    }

    @Test
    @WithAnonymousUser
    void createAsAnonymousUserTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.create(createLearningSetItemDto, 1L)
        );
    }
    // endregion

    // region replace
    @Test
    @WithUserDetails("user1")
    void replaceAsOwnerTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException,
               LearningSetItemModificationAccessForbiddenException {
        LearningSetItemDto replaced = itemService.replace(createLearningSetItemDto, 1L, 1L);

        assertNotNull(replaced);

        LearningSetItemDto retrieved = itemService.findOne(1L, 1L);

        assertNotNull(retrieved);
        assertEquals(replaced.getTerm(), retrieved.getTerm());
        assertEquals(replaced.getTranslation(), retrieved.getTranslation());
    }

    @Test
    @WithUserDetails("user2")
    void replaceAsEditorTest()
        throws LearningSetNotFoundException, LearningSetItemNotFoundException, LearningSetAccessForbiddenException,
               LearningSetItemModificationAccessForbiddenException {
        LearningSetItemDto replaced = itemService.replace(createLearningSetItemDto, 1L, 1L);
        LearningSetItemDto retrieved = itemService.findOne(1L, 1L);

        assertNotNull(replaced);
        assertEquals("term", replaced.getTerm());
        assertEquals("translation", replaced.getTranslation());

        assertNotNull(retrieved);
        assertEquals(replaced.getTerm(), retrieved.getTerm());
        assertEquals(replaced.getTranslation(), retrieved.getTranslation());
    }

    @Test
    @WithUserDetails("user3")
    void replaceAsPropserTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.replace(createLearningSetItemDto, 1L, 1L)
        );
    }

    @Test
    @WithUserDetails("user1")
    void replaceAsReaderTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.replace(createLearningSetItemDto, 2L, 1L)
        );
    }

    @Test
    @WithAnonymousUser
    void replaceAsAnonumousUserTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.replace(createLearningSetItemDto, 1L, 1L)
        );
    }
    // endregion

    // region delete
    @Test
    @WithUserDetails("user1")
    void deleteAsOwnerTest() {
        assertDoesNotThrow(() -> itemService.delete(1L, 1L));

        assertThrows(LearningSetItemNotFoundException.class,
                     () -> itemService.findOne(1L, 1L)
        );

        assertThrows(LearningSetItemNotFoundException.class,
                     () -> itemService.delete(1L, 1L)
        );
    }

    @Test
    @WithUserDetails("user2")
    void deleteAsEditorTest() {
        assertDoesNotThrow(() -> itemService.delete(1L, 1L));

        assertThrows(LearningSetItemNotFoundException.class,
                     () -> itemService.findOne(1L, 1L)
        );

        assertThrows(LearningSetItemNotFoundException.class,
                     () -> itemService.delete(1L, 1L)
        );
    }

    @Test
    @WithUserDetails("user3")
    void deleteAsProposerTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.delete(1L, 1L)
        );
    }

    @Test
    @WithUserDetails("user1")
    void deleteAsReaderTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.delete(2L, 1L)
        );
    }

    @Test
    @WithAnonymousUser
    void deleteAsAnonymousUserTest() {
        assertThrows(LearningSetItemModificationAccessForbiddenException.class,
                     () -> itemService.delete(1L, 1L)
        );
    }
    // endregion
}
