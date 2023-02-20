package pl.lodz.p.it.wordapp.exception;

public class LearningSetAccessForbiddenException extends BaseApplicationException {
    public LearningSetAccessForbiddenException() {
        super(LEARNING_SET_ACCESS_FORBIDDEN);
    }
}
