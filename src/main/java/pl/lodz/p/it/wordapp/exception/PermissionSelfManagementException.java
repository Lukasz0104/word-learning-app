package pl.lodz.p.it.wordapp.exception;

public class PermissionSelfManagementException extends BaseApplicationException {
    public PermissionSelfManagementException() {
        super("You cannot edit your own permissions");
    }
}
