package com.example.hotel.controller.command.validation;

public class InvalidInputException extends Exception{
    private static final long serialVersionUID = -7811717792670888816L;

    public InvalidInputException() {
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
