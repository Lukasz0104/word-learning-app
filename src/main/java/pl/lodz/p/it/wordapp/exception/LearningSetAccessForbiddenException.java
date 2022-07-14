package pl.lodz.p.it.wordapp.exception;

public class LearningSetAccessForbiddenException extends RuntimeException {
    public LearningSetAccessForbiddenException(Long id) {
        super(String.format("You have no access to set with id=%d", id));
    }
}
