package pl.lodz.p.it.wordapp.exception;

import pl.lodz.p.it.wordapp.model.LearningSetItemKey;

public class LearningSetItemNotFoundException extends BaseApplicationException {
    public LearningSetItemNotFoundException(Long setID, Long itemID) {
        super(String.format("Item with id=%d does not exist in set with id=%d", setID, itemID));
    }

    public LearningSetItemNotFoundException(LearningSetItemKey key) {
        this(key.getSetID(), key.getItemID());
    }
}
