package com.eni.bookhub.mapper;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.dto.BookDTO;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookDTO toDTO(Book book) {
        if (book == null) return null;
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getLastName(),
                book.getFirstName(),
                book.getIsbn(),
                book.getCategory(),
                book.getStatus(),
                book.getFrontCoverImg(),
                book.getCopyNumber()
        );
    }

    public Book toEntity(BookDTO dto) {
        if (dto == null) return null;
        return Book.builder()
                .id(dto.id())
                .title(dto.title())
                .lastName(dto.lastName())
                .firstName(dto.firstName())
                .isbn(dto.isbn())
                .category(dto.category())
                .status(dto.status())
                .frontCoverImg(dto.frontCoverImg())
                .copyNumber(dto.copyNumber())
                .build();
    }
}
