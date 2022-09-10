package pl.lodz.p.it.wordapp.exception;

public class UserAlreadyExistsException extends BaseApplicationException {
    public UserAlreadyExistsException() {
        super(USERNAME_ALREADY_TAKEN);
    }
}
