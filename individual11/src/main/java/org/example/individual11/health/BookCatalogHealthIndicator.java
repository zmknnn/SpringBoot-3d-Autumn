package org.example.individual11.health;

import org.example.individual11.repository.BookRepository;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;

@Component
public class BookCatalogHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    public BookCatalogHealthIndicator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        try {
            long totalBooks = bookRepository.count();

            return Health.up()
                    .withDetail("service", "Book Catalog")
                    .withDetail("totalBooks", totalBooks)
                    .withDetail("message", "Book catalog is available")
                    .build();

            /*throw new DataAccessResourceFailureException(
                    "Simulated database failure"
            );*/

        } catch (DataAccessException exception) {

            return Health.down()
                    .withDetail("service", "Book Catalog")
                    .withDetail("error", exception.getMessage())
                    .build();
        }
    }
}

