package com.damnteam.letmechat.error;

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

    private final MessageSource messageSource;

    public AppExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleCommonException(final Exception ex, final WebRequest request) {
        final GenericResponse genericResponse = new GenericResponse(messageSource.getMessage("error.message", null, request.getLocale()), "Internal Error");
        return new ResponseEntity<>(genericResponse, HttpStatus.I_AM_A_TEAPOT);
    }
}
