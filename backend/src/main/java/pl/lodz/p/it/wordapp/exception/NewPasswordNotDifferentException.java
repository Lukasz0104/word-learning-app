package pl.lodz.p.it.wordapp.exception;

public class NewPasswordNotDifferentException extends BaseApplicationException {
    public NewPasswordNotDifferentException() {
        super(NEW_PASSWORD_NOT_DIFFERENT);
    }
}
