package com.eni.bookhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
