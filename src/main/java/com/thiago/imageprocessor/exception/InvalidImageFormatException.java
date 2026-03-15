package com.thiago.imageprocessor.exception;

public class InvalidImageFormatException extends RuntimeException{
    public InvalidImageFormatException(String message){
        super(message);
    }
}
