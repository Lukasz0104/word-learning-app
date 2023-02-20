package pl.lodz.p.it.wordapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import pl.lodz.p.it.wordapp.controller.dto.PermissionsDto;
import pl.lodz.p.it.wordapp.controller.dto.UserPermissionsDto;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetPermissionAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.PermissionManagementAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.PermissionSelfManagementException;
import pl.lodz.p.it.wordapp.exception.UserNotFoundException;
import pl.lodz.p.it.wordapp.model.AccessRole;
import pl.lodz.p.it.wordapp.model.Role;
import pl.lodz.p.it.wordapp.repository.AccessRoleRepository;

@SpringBootTest
@Transactional
@DirtiesContext
class LearningSetPermissionServiceTest {
    private PermissionsDto perm;

    @Autowired
    private LearningSetPermissionService permissionService;

    @Autowired
    private AccessRoleRepository accessRoleRepository;

    // region getPermissions
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
    // endregion

    // region getPermissionForUser
    @Test
    @WithUserDetails("user1")
    void getPermissionForUserAsOwnerTest() throws LearningSetPermissionAccessForbiddenException {
        perm = permissionService.getPermissionsForUser(1L, 2L);
        assertTrue(perm.isAbleToEdit());
        assertFalse(perm.isAbleToManage());

        perm = permissionService.getPermissionsForUser(1L, 3L);
        assertTrue(perm.isAbleToProposeChanges());
        assertFalse(perm.isAbleToEdit());
    }

    @Test
    @WithUserDetails("user2")
    void getPermissionForUserNotAsOwnerTest() {
        assertThrows(
            LearningSetPermissionAccessForbiddenException.class,
            () -> permissionService.getPermissionsForUser(1L, 2L)
        );
    }
    // endregion

    // region getPermissionsForUsers
    @Test
    @WithUserDetails("user3")
    void getPermissionForUsersAsOwnerTest() throws LearningSetPermissionAccessForbiddenException {
        List<UserPermissionsDto> retrievedPermissions = permissionService.getPermissionsForUsers(3L);

        assertNotNull(retrievedPermissions);
        assertEquals(2, retrievedPermissions.size());

        assertEquals(1L, retrievedPermissions.get(0).getUserId());
        assertEquals("user1", retrievedPermissions.get(0).getUsername());
        assertTrue(retrievedPermissions.get(0).isAbleToRead());
        assertFalse(retrievedPermissions.get(0).isAbleToProposeChanges());


        assertEquals(4L, retrievedPermissions.get(1).getUserId());
        assertEquals("user4", retrievedPermissions.get(1).getUsername());
        assertTrue(retrievedPermissions.get(1).isAbleToProposeChanges());
        assertFalse(retrievedPermissions.get(1).isAbleToEdit());
    }

    @Test
    @WithUserDetails("user2")
    void getPermissionsForUsersNotAsOwnerTest() {
        assertThrows(
            LearningSetPermissionAccessForbiddenException.class,
            () -> permissionService.getPermissionsForUsers(1L)
        );
    }
    // endregion

    // region Add Read Permission
    @Test
    @WithUserDetails("user1")
    void addReadPermissionAsOwnerTest()
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
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
        );
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
    // endregion

    // region Delete Read Permission
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

        permissionService.deleteReadPermission(2L, 1L);

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
            () -> permissionService.deleteReadPermission(2L, 3L)
        );
    }

    @Test
    @WithUserDetails("user2")
    void deleteReadPermissionFromSelfTest() {
        assertThrows(
            PermissionSelfManagementException.class,
            () -> permissionService.deleteReadPermission(2L, 2L)
        );

        AccessRole accessRole = accessRoleRepository
                                    .findBySet_IdAndUser_Id(2L, 2L)
                                    .orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.OWNER, accessRole.getRole());
    }
    // endregion

    // region Add Propopose Permission
    @Test
    @WithUserDetails("user1")
    void addProposePermissionAsOwnerTest()
        throws PermissionManagementAccessForbiddenException, UserNotFoundException, LearningSetNotFoundException {
        AccessRole accessRole;

        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 4L).orElse(null);
        assertNull(accessRole);

        permissionService.addProposePermission(1L, 4L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 4L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.PROPOSER, accessRole.getRole());

        permissionService.addProposePermission(1L, 4L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 4L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.PROPOSER, accessRole.getRole());
    }

    @Test
    @WithUserDetails("user1")
    void addProposePermissionNotAsOwnerTest() {
        assertThrows(
            PermissionManagementAccessForbiddenException.class,
            () -> permissionService.addProposePermission(2L, 3L)
        );
    }

    @Test
    @WithUserDetails("user1")
    void addProposePermissionShouldNotAlterHigherPermissionTest()
        throws PermissionManagementAccessForbiddenException, UserNotFoundException, LearningSetNotFoundException {
        permissionService.addProposePermission(1L, 2L);

        AccessRole accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 2L)
                                                    .orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.EDITOR, accessRole.getRole());
    }
    // endregion

    // region Delete Propose Permission
    @Test
    @WithUserDetails("user1")
    void deleteProposePermissionAsOwnerTest()
        throws PermissionManagementAccessForbiddenException, PermissionSelfManagementException {
        AccessRole accessRole;
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 3L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.PROPOSER, accessRole.getRole());

        permissionService.deleteProposePermission(1L, 3L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 3L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.READER, accessRole.getRole());

        permissionService.deleteProposePermission(1L, 3L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 3L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.READER, accessRole.getRole());
    }

    @Test
    @WithUserDetails("user1")
    void deleteProposePermissionsNotAsOwnerTest() {
        assertThrows(
            PermissionManagementAccessForbiddenException.class,
            () -> permissionService.deleteProposePermission(2L, 3L)
        );
    }

    @Test
    @WithUserDetails("user1")
    void deleteProposePermissionShouldDemoteRoleTest()
        throws PermissionManagementAccessForbiddenException, PermissionSelfManagementException {
        AccessRole accessRole;
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 2L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.EDITOR, accessRole.getRole());

        permissionService.deleteProposePermission(1L, 2L);

        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 2L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.READER, accessRole.getRole());
    }
    // endregion

    // region Add Edit Permission
    @Test
    @WithUserDetails("user1")
    void addEditPermissionAsOwnerToExistingRuleTest()
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        AccessRole accessRole;
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 3L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.PROPOSER, accessRole.getRole());

        permissionService.addEditPermission(1L, 3L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 3L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.EDITOR, accessRole.getRole());

        permissionService.addEditPermission(1L, 3L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 3L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.EDITOR, accessRole.getRole());
    }

    @Test
    @WithUserDetails("user1")
    void addEditPermissionAsOwnerNewRuleTest()
        throws UserNotFoundException, LearningSetNotFoundException, PermissionManagementAccessForbiddenException {
        AccessRole accessRole;
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 4L).orElse(null);

        assertNull(accessRole);

        permissionService.addEditPermission(1L, 4L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 4L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.EDITOR, accessRole.getRole());
    }

    @Test
    @WithUserDetails("user2")
    void addEditPermissionNotAsOwnerTest() {
        assertThrows(
            PermissionManagementAccessForbiddenException.class,
            () -> permissionService.addEditPermission(1L, 2L)
        );
    }
    // endregion

    // region Delete Edit Permission
    @Test
    @WithUserDetails("user1")
    void deleteEditPermissionAsOwnerTest()
        throws PermissionSelfManagementException, PermissionManagementAccessForbiddenException {
        AccessRole accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 2L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.EDITOR, accessRole.getRole());

        permissionService.deleteEditPermission(1L, 2L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 2L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.PROPOSER, accessRole.getRole());

        permissionService.deleteEditPermission(1L, 2L);
        accessRole = accessRoleRepository.findBySet_IdAndUser_Id(1L, 2L).orElse(null);

        assertNotNull(accessRole);
        assertEquals(Role.PROPOSER, accessRole.getRole());
    }

    @Test
    @WithUserDetails("user3")
    void deleteEditPermissionNotAsOwnerTest() {
        assertThrows(
            PermissionSelfManagementException.class,
            () -> permissionService.deleteEditPermission(1L, 3L)
        );
    }

    @Test
    @WithUserDetails("user1")
    void deleteEditPermissionAsOwnerSelfPermissionTest() {
        assertThrows(
            PermissionSelfManagementException.class,
            () -> permissionService.deleteEditPermission(1L, 1L)
        );
    }
    // endregion
}
