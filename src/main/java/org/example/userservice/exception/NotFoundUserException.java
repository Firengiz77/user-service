package org.example.userservice.exception;

public class NotFoundUserException extends RuntimeException  {

    private final static String MESSAGE = "Email not found";

    public NotFoundUserException() {
        super(MESSAGE);
    }

}
