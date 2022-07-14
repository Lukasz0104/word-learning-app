package pl.lodz.p.it.wordapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;

@SpringBootTest
class LearningSetServiceTest {

    @Autowired
    private LearningSetService service;

    @Test
    @WithAnonymousUser
    void findOneAnonymousUserTest() throws LearningSetAccessForbiddenException {
        LearningSetDetailsDto ls1 = service.findOne(1L);
        assertEquals(ls1.getId(), 1L);
        assertTrue(ls1.isPubliclyVisible());

        assertThrows(LearningSetAccessForbiddenException.class, () -> service.findOne(2L));

        assertThrows(LearningSetAccessForbiddenException.class, () -> service.findOne(3L));
    }

    @Test
    @WithUserDetails("user1")
    void findOneForUser1Test() throws LearningSetAccessForbiddenException {
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
    void findOneForUser2Test() throws LearningSetAccessForbiddenException {
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
}
