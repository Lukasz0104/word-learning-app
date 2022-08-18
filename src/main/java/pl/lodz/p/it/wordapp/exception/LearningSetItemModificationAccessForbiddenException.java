package pl.lodz.p.it.wordapp.exception;

public class LearningSetItemModificationAccessForbiddenException extends BaseApplicationException {
    public LearningSetItemModificationAccessForbiddenException(Long setId) {
        super(String.format("You have no access to edit items in set %d", setId));
    }
}
