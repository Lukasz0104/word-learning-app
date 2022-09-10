package pl.lodz.p.it.wordapp.exception;

public class IncorrectPasswordException extends BaseApplicationException {
    public IncorrectPasswordException() {
        super(INVALID_PASSWORD);
    }
}
