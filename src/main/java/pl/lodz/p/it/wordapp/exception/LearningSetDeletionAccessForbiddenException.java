package pl.lodz.p.it.wordapp.exception;

public class LearningSetDeletionAccessForbiddenException extends BaseApplicationException {
    public LearningSetDeletionAccessForbiddenException(Long setId) {
        super(String.format("Insufficient permissions to delete set with id=%d", setId));
    }
}
