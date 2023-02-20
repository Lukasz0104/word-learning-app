package pl.lodz.p.it.wordapp.exception;

public class EmailAddressNotDifferentException extends BaseApplicationException {
    public EmailAddressNotDifferentException() {
        super(EMAIL_ADDRESS_NOT_DIFFERENT_FORM_OLD);
    }
}
