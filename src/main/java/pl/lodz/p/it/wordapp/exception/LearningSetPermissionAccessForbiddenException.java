package pl.lodz.p.it.wordapp.exception;

public class LearningSetPermissionAccessForbiddenException extends BaseApplicationException {
    public LearningSetPermissionAccessForbiddenException() {
        super("Only set owner can access user permissions");
    }
}
