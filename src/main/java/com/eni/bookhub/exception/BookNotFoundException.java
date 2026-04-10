package com.eni.bookhub.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) {
        super("Aucun livre trouvé avec l'ID " + id + ".");
    }
}
