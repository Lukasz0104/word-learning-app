package pl.lodz.p.it.wordapp.exception;

public class LearningSetPermissionAccessForbiddenException extends BaseApplicationException {
    public LearningSetPermissionAccessForbiddenException() {
        super(LEARNING_SET_USERS_PERMISSION_ACCESS_FORBIDDEN);
    }
}
