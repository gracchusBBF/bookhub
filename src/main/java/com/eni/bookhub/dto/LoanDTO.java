package com.eni.bookhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanDTO {
    private Integer id;
    private Timestamp startDate;
    private Date returnDate;
    private Date deadline;
    private String userEmail;
    private Integer bookId;
    private String bookTitle;
}