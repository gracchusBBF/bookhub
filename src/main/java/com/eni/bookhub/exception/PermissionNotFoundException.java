package com.eni.bookhub.exception;

public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(int id) {
        super("Aucune permission n'a été trouvé avec l'ID " + id + ".");
    }
}
