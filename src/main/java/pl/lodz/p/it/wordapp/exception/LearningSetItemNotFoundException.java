package pl.lodz.p.it.wordapp.exception;

import pl.lodz.p.it.wordapp.model.LearningSetItemKey;

public class LearningSetItemNotFoundException extends BaseApplicationException {
    public LearningSetItemNotFoundException(Long setID, Long itemID) {
        super(String.format(LEARNING_SET_ITEM_NOT_FOUND, setID, itemID));
    }

    public LearningSetItemNotFoundException(LearningSetItemKey key) {
        this(key.getSetID(), key.getItemID());
    }
}
