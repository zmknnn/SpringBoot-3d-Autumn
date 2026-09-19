package org.example.individual3.strategy;
import org.example.individual3.entity.Book;
import org.example.individual3.entity.BookStatus;
import org.springframework.stereotype.Component;

@Component
public class AvailableToReservedStrategy implements StatusTransitionStrategy {

    @Override
    public boolean supports(BookStatus from, BookStatus to) {
        return from == BookStatus.AVAILABLE
                && to == BookStatus.RESERVED;
    }

    @Override
    public void apply(Book book) {
        book.setStatus(BookStatus.RESERVED);
    }
}
