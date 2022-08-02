package pl.lodz.p.it.wordapp.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import pl.lodz.p.it.wordapp.controller.dto.PermissionsDto;

@SpringBootTest
@Transactional
class LearningSetPermissionServiceTest {
    private PermissionsDto perm;

    @Autowired
    private LearningSetPermissionService permissionService;

    @Test
    @WithAnonymousUser
    void getPermissionsAnonymousUserTest() {
        perm = permissionService.getPermissions(1L);
        assertTrue(perm.isAbleToRead());
        assertFalse(perm.isAbleToProposeChanges());
        assertFalse(perm.isAbleToEdit());
        assertFalse(perm.isAbleToManage());

        perm = permissionService.getPermissions(2L);
        assertFalse(perm.isAbleToRead());
        assertFalse(perm.isAbleToProposeChanges());
        assertFalse(perm.isAbleToEdit());
        assertFalse(perm.isAbleToManage());
    }

    @Test
    @WithUserDetails("user1")
    void getPermissionsUser1Test() {
        perm = permissionService.getPermissions(1L);
        assertTrue(perm.isAbleToRead());
        assertTrue(perm.isAbleToProposeChanges());
        assertTrue(perm.isAbleToEdit());
        assertTrue(perm.isAbleToManage());

        perm = permissionService.getPermissions(3L);
        assertTrue(perm.isAbleToRead());
        assertFalse(perm.isAbleToProposeChanges());
        assertFalse(perm.isAbleToEdit());
        assertFalse(perm.isAbleToManage());
    }

    @Test
    @WithUserDetails("user4")
    void getPermissionsUser4Test() {
        perm = permissionService.getPermissions(1L);
        assertTrue(perm.isAbleToRead());
        assertFalse(perm.isAbleToProposeChanges());
        assertFalse(perm.isAbleToEdit());
        assertFalse(perm.isAbleToManage());

        perm = permissionService.getPermissions(2L);
        assertTrue(perm.isAbleToRead());
        assertTrue(perm.isAbleToProposeChanges());
        assertTrue(perm.isAbleToEdit());
        assertFalse(perm.isAbleToManage());

        perm = permissionService.getPermissions(3L);
        assertTrue(perm.isAbleToRead());
        assertTrue(perm.isAbleToProposeChanges());
        assertFalse(perm.isAbleToEdit());
        assertFalse(perm.isAbleToManage());
    }
}
