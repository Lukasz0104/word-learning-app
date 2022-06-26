package pl.lodz.p.it.wordapp.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lodz.p.it.wordapp.exception.LearningSetNotFoundException;

@ControllerAdvice
public class LearningSetExceptionHandler {

    @ExceptionHandler(LearningSetNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String learningSetNotFoundHandler(LearningSetNotFoundException ex) {
        return ex.getMessage();
    }



}
