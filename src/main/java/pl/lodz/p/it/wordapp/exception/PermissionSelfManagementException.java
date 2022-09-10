package pl.lodz.p.it.wordapp.exception;

public class PermissionSelfManagementException extends BaseApplicationException {
    public PermissionSelfManagementException() {
        super(PERMISSIONS_SELF_MANAGEMENT_ERROR);
    }
}
