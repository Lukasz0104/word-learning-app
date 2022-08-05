package pl.lodz.p.it.wordapp.exception;

public class LearningSetNotFoundException extends BaseApplicationException {
    public LearningSetNotFoundException(Long id) {
        super(String.format("Learning set with id=%d does not exist!", id));
    }
}
