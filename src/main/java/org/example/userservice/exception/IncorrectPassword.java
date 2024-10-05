package org.example.userservice.exception;

public class IncorrectPassword extends RuntimeException{

    private  final  static String MESSAGE = "Incorrect password for this email : ";

    public IncorrectPassword(String email) {
        super(MESSAGE + email);
    }

}
