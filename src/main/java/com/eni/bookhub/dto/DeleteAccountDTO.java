package com.eni.bookhub.dto;

import lombok.Data;

@Data
public class DeleteAccountDTO {
    private String email;
    private String password;
}
