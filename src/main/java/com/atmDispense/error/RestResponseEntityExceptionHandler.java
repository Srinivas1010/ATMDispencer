package com.atmDispense.error;

import com.atmDispense.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ATMCashDispenserException.class)
    public ResponseEntity<ErrorMessage> atmCashDispenserException(ATMCashDispenserException exception,
                                                                  WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.CONFLICT,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(message);
    }
}
