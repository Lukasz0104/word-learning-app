package pl.lodz.p.it.wordapp.exception;

public class EmailAddressAlreadyTakenException extends BaseApplicationException {
    public EmailAddressAlreadyTakenException() {
        super(EMAIL_ADDRESS_ALREADY_TAKEN);
    }
}
