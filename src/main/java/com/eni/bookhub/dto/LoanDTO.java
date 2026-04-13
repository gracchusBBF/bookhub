package com.eni.bookhub.dto;

import lombok.Data;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class LoanDTO {
    private Integer id;
    private Timestamp startDate;
    private Date returnDate;
    private Date deadline;
    private Integer userId;
    private Integer bookId;
    private String bookTitle;
}