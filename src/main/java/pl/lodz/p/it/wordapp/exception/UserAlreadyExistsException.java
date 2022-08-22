package pl.lodz.p.it.wordapp.exception;

public class UserAlreadyExistsException extends BaseApplicationException {
    public UserAlreadyExistsException() {
        super("Account with this username already exists");
    }
}
