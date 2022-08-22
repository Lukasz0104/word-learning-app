package pl.lodz.p.it.wordapp.exception;

public class EmailAddressAlreadyTakenException extends BaseApplicationException {
    public EmailAddressAlreadyTakenException() {
        super("This email address is already taken");
    }
}
