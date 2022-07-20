package com.ElibraryDevelopment.Elibrary.Exception;


import com.ElibraryDevelopment.Elibrary.Model.Response.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = {ClientSideException.class})
    public ResponseEntity<Object> handleUserServiceException(ClientSideException ex, WebRequest request){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Message message = new Message(new Date(), ex.getMessage(),httpStatus);
        return new ResponseEntity<>(message, new HttpHeaders(), httpStatus);
    }
}
