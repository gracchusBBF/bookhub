package com.eni.bookhub.exception;

public class DuplicateIsbnException extends RuntimeException {
    public DuplicateIsbnException(String isbn) {
        super("Un livre avec l'isbn '" + isbn + "' existe déjà.");
    }
}
