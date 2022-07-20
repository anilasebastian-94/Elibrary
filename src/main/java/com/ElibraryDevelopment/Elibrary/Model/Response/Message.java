package com.ElibraryDevelopment.Elibrary.Model.Response;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class Message {

    private Date timeStamp;
    private String message;
    private HttpStatus httpStatus;

    public Message(){}

    public Message(Date timeStamp, String message, HttpStatus httpStatus) {
        this.timeStamp = timeStamp;
        this.message=message;
        this.httpStatus=httpStatus;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
