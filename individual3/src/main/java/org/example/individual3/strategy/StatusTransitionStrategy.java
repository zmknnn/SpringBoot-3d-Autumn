package org.example.individual3.strategy;

import org.example.individual3.entity.Book;
import org.example.individual3.entity.BookStatus;

public interface StatusTransitionStrategy {

    boolean supports(BookStatus from, BookStatus to);

    void apply(Book book);
}