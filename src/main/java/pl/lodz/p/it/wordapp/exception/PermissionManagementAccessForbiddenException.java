package pl.lodz.p.it.wordapp.exception;

public class PermissionManagementAccessForbiddenException extends BaseApplicationException {
    public PermissionManagementAccessForbiddenException() {
        super(LEARNING_SET_PERMISSION_MANAGEMENT_ACCESS_FORBIDDEN);
    }
}
