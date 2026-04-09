package com.eni.bookhub.BO;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="comment")
public class Comment {
    @Id
    @Column(name="comment_id")
    private int id;

    @Column(name="rate", nullable=false)
    private int rate;

    @Column(name="comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;




}
