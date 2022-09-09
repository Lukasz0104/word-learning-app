package pl.lodz.p.it.wordapp.exception;

public class IncorrectEmailAddressException extends BaseApplicationException {
    public IncorrectEmailAddressException() {
        super("Invalid email address");
    }
}
