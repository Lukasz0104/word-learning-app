package pl.lodz.p.it.wordapp.exception;

public class EmailAddressNotDifferentException extends BaseApplicationException {
    public EmailAddressNotDifferentException() {
        super("New email address must differ from the old email address");
    }
}
