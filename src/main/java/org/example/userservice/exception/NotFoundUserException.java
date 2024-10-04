package org.example.userservice.exception;

public class NotFoundUserException extends RuntimeException  {
    private final static String MESSAGE = "User not found";

    public NotFoundUserException(Long id) {
        super((MESSAGE + id));
    }
}
