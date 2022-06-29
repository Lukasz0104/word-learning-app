package pl.lodz.p.it.wordapp.exception;

public class LearningSetItemNotFoundException extends RuntimeException {
    public LearningSetItemNotFoundException(Long setID, Long itemID) {
        super(String.format("Item with id=%d does not exist in set id=%d", setID, itemID));
    }
}
