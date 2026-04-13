package com.eni.bookhub.exception;

public class InvalidBookIdException extends RuntimeException {
    public InvalidBookIdException(String id) {
        super("L'ID '" + id + "' est invalide.");
    }
}
