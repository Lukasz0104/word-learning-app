package pl.lodz.p.it.wordapp.exception;

public class NewPasswordNotDifferentException extends BaseApplicationException {
    public NewPasswordNotDifferentException() {
        super("New password must differ from the old password!");
    }
}
