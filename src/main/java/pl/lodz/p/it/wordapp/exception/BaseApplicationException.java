package pl.lodz.p.it.wordapp.exception;

public abstract class BaseApplicationException extends Exception {
    public BaseApplicationException(String message) {
        super(message);
    }
}
