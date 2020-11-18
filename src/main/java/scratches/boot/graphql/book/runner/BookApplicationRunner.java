package scratches.boot.graphql.book.runner;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import scratches.boot.graphql.book.domain.Author;
import scratches.boot.graphql.book.domain.Book;
import scratches.boot.graphql.book.domain.BookRepository;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@Component
@AllArgsConstructor
public class BookApplicationRunner implements ApplicationRunner {

    private final BookRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        var author = new Author("Rudyard Kipling");

        repository.saveAll(
                List.of(
                        new Book(author, "If"),
                        new Book(author, "The Jungle Book")
                )
        );
    }
}
