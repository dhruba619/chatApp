package org.tomlang.livechat.exceptions;

import java.time.Instant;
import java.util.Date;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {
    
    
    @ExceptionHandler(LiveChatException.class)
    public final ResponseEntity<LiveChatError> handleLiveChatException(LiveChatException ex, WebRequest request) {
        LiveChatError errorDetails = new LiveChatError(ex.getErrorDesciption(), new Date(Instant.now().toEpochMilli()));
        return new ResponseEntity<>(errorDetails, ex.getHttpCode());
    }
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<LiveChatError> handleException(LiveChatException ex, WebRequest request) {
        LiveChatError errorDetails = new LiveChatError(ex.getMessage(), new Date(Instant.now()
            .getEpochSecond()));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
      
}
