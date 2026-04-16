package com.eni.bookhub.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDTO {

    @NotNull
    @Min(1) @Max(5)
    private int rate;

    @Size(max = 500)
    private String comment;

    @NotNull
    private String userEmail;

    @NotNull
    private int bookId;

}