package pl.lodz.p.it.wordapp.exception;

public class PermissionManagementAccessForbiddenException extends BaseApplicationException {
    public PermissionManagementAccessForbiddenException() {
        super("Only set owner can edit permissions");
    }
}
