package pl.lodz.p.it.wordapp.exception;

public class UserNotFoundException extends BaseApplicationException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id=%d could not be found", userId));
    }
}
