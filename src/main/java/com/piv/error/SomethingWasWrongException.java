package com.piv.error;

public class SomethingWasWrongException extends ApplicationException{
    public SomethingWasWrongException(String message) {
        super(message);
    }
}
