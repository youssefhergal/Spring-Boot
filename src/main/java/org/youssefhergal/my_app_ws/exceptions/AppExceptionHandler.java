package org.youssefhergal.my_app_ws.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.youssefhergal.my_app_ws.responses.ErrorMessage;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({UserException.class})
    public ResponseEntity<Object> handleUserException(UserException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new java.util.Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new java.util.Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
