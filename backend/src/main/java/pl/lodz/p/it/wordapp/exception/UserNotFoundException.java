package pl.lodz.p.it.wordapp.exception;

public class UserNotFoundException extends BaseApplicationException {
    public UserNotFoundException(Long userId) {
        super(String.format(USER_NOT_FOUND, userId));
    }
}
