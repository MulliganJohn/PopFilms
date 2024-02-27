package com.popfilms.popfilmsapi.util.Exceptions;

public class InvalidReviewException extends RuntimeException{
    public InvalidReviewException(String message){
        super(message);
    }
}
