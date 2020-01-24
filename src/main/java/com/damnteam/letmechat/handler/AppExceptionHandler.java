package com.damnteam.letmechat.handler;

import com.damnteam.letmechat.error.LoginAlreadyTakenException;
import com.damnteam.letmechat.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({LoginAlreadyTakenException.class})
    public ResponseEntity<Object> handleLoginAlreadyTakenException(final Exception ex) {
        final GenericResponse genericResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName());
        return new ResponseEntity<>(genericResponse, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleCommonException(final Exception ex, final WebRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage() : messageSource.getMessage("error.message", null, request.getLocale());
        final GenericResponse genericResponse = new GenericResponse(message, "Internal Error");
        return new ResponseEntity<>(genericResponse, HttpStatus.I_AM_A_TEAPOT);
    }
}
