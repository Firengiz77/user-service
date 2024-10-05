package org.example.userservice.exception;

public class PermissionDeniedException extends RuntimeException{

    private static final String MESSAGE ="You don't have permission";

    public PermissionDeniedException() {
        super(MESSAGE);
    }

}
