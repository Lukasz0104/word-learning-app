package pl.lodz.p.it.wordapp.exception;

public class LearningSetNotFoundException extends BaseApplicationException {
    public LearningSetNotFoundException(Long id) {
        super(String.format(LEARNING_SET_NOT_FOUND, id));
    }
}
