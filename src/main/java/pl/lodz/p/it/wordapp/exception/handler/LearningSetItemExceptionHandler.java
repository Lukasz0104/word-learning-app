package pl.lodz.p.it.wordapp.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lodz.p.it.wordapp.exception.LearningSetItemNotFoundException;

@ControllerAdvice
public class LearningSetItemExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LearningSetItemNotFoundException.class)
    @ResponseBody
    public String handleLearningSetItemNotFoundException(LearningSetItemNotFoundException ex) {
        return ex.getMessage();
    }
}
