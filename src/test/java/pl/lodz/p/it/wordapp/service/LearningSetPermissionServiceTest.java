package pl.lodz.p.it.wordapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import pl.lodz.p.it.wordapp.controller.dto.PermissionsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.exception.PermissionManagementAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.PermissionSelfManagementException;
import pl.lodz.p.it.wordapp.exception.UserNotFoundException;
import pl.lodz.p.it.wordapp.model.AccessRole;
import pl.lodz.p.it.wordapp.model.Role;
import pl.lodz.p.it.wordapp.repository.AccessRoleRepository;

@SpringBootTest
@Transactional
class LearningSetPermissionServiceTest {
    private PermissionsDto perm;

    @Autowired
    private LearningSetPermissionService permissionService;

    @Autowired
    private AccessRoleRepository accessRoleRepository;

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

    @Test
    @WithUserDetails("user1")
    void addReadPermissionAsOwnerTest() throws UserNotFoundException,
        LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        AccessRole accessRole;
        accessRole = accessRoleRepository
            .findBySet_IdAndUser_Id(1L, 4L)
            .orElse(null);

        assertNull(accessRole);

        permissionService.addReadPermission(1L, 4L);
        accessRole = accessRoleRepository
            .findBySet_IdAndUser_Id(1L, 4L)
            .orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.READER, accessRole.getRole());

        // repeating should not change anything
        permissionService.addReadPermission(1L, 4L);

        assertEquals(Role.READER, accessRole.getRole());
    }

    @Test
    @WithUserDetails("user2")
    void addReadPermissionNotAsOwnerTest() {
        assertThrows(
            PermissionManagementAccessForbiddenException.class,
            () -> permissionService.addReadPermission(1L, 4L)
        )
        ;
    }

    @Test
    @WithAnonymousUser
    void addReadPermissionAnonymousUserTest() {
        assertThrows(
            PermissionManagementAccessForbiddenException.class,
            () -> permissionService.addReadPermission(1L, 4L)
        );
    }

    @Test
    @WithUserDetails("user1")
    void addReadPermissionNonExistentUserTest() {
        assertThrows(
            UserNotFoundException.class,
            () -> permissionService.addReadPermission(1L, 10L)
        );
    }

    @Test
    @WithUserDetails("user2")
    void deleteReadPermissionAsOwnerTest()
        throws PermissionSelfManagementException, PermissionManagementAccessForbiddenException {
        AccessRole accessRole;
        accessRole = accessRoleRepository
            .findBySet_IdAndUser_Id(2L, 1L)
            .orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.READER, accessRole.getRole());

        permissionService.deleteAddPermission(2L, 1L);

        accessRole = accessRoleRepository
            .findBySet_IdAndUser_Id(2L, 1L)
            .orElse(null);

        assertNull(accessRole);
    }

    @Test
    @WithUserDetails("user1")
    void deleteReadPermissionNotAsOwnerTest() {
        assertThrows(
            PermissionManagementAccessForbiddenException.class,
            () -> permissionService.deleteAddPermission(2L, 1L)
        );
    }

    @Test
    @WithUserDetails("user2")
    void deleteReadPermissionFromSelfTest() {
        assertThrows(
            PermissionSelfManagementException.class,
            () -> permissionService.deleteAddPermission(2L, 2L)
        );

        AccessRole accessRole = accessRoleRepository
            .findBySet_IdAndUser_Id(2L, 2L)
            .orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.OWNER, accessRole.getRole());
    }
}
