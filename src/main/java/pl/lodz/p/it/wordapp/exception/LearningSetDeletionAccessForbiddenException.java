package pl.lodz.p.it.wordapp.exception;

public class LearningSetDeletionAccessForbiddenException extends BaseApplicationException {
    public LearningSetDeletionAccessForbiddenException() {
        super(LEARNING_SET_DELETION_ACCESS_FORBIDDEN);
    }
}
