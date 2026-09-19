package org.example.individual3.strategy;

import org.example.individual3.entity.Book;
import org.example.individual3.entity.BookStatus;
import org.springframework.stereotype.Component;

@Component
public class BorrowedToAvailableStrategy implements StatusTransitionStrategy{

    @Override
    public boolean supports(BookStatus from, BookStatus to){
        return from == BookStatus.BORROWED
                && to == BookStatus.AVAILABLE;
    }
    @Override
    public void apply(Book book) {
        book.setStatus(BookStatus.AVAILABLE);
    }
}
