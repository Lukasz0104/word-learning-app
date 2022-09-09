package pl.lodz.p.it.wordapp.exception.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lodz.p.it.wordapp.exception.BaseApplicationException;
import pl.lodz.p.it.wordapp.exception.EmailAddressAlreadyTakenException;
import pl.lodz.p.it.wordapp.exception.EmailAddressNotDifferentException;
import pl.lodz.p.it.wordapp.exception.IncorrectEmailAddressException;
import pl.lodz.p.it.wordapp.exception.IncorrectPasswordException;
import pl.lodz.p.it.wordapp.exception.LearningSetAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetDeletionAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetItemModificationAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.LearningSetItemNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;
import pl.lodz.p.it.wordapp.exception.LearningSetPermissionAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.NewPasswordNotDifferentException;
import pl.lodz.p.it.wordapp.exception.PermissionManagementAccessForbiddenException;
import pl.lodz.p.it.wordapp.exception.PermissionSelfManagementException;
import pl.lodz.p.it.wordapp.exception.UserAlreadyExistsException;
import pl.lodz.p.it.wordapp.exception.UserNotFoundException;

@ControllerAdvice
public class LearningSetExceptionHandler {

    @ExceptionHandler({
        LearningSetItemNotFoundException.class,
        LearningSetNotFoundException.class,
        UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFoundExceptionHandler(BaseApplicationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
          .getAllErrors()
          .forEach((error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
          });
        return errors;
    }

    @ExceptionHandler({
        PermissionSelfManagementException.class,
        IncorrectPasswordException.class,
        NewPasswordNotDifferentException.class,
        IncorrectEmailAddressException.class,
        EmailAddressNotDifferentException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String permissionSelfManagementExceptionHandler(BaseApplicationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({
        LearningSetAccessForbiddenException.class,
        LearningSetDeletionAccessForbiddenException.class,
        PermissionManagementAccessForbiddenException.class,
        LearningSetPermissionAccessForbiddenException.class,
        LearningSetItemModificationAccessForbiddenException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String learningSetAccessForbiddenHandler(BaseApplicationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({
        UserAlreadyExistsException.class,
        EmailAddressAlreadyTakenException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public String UserAlreadyExistsExceptionHandler(BaseApplicationException ex) {
        return ex.getMessage();
    }
}
