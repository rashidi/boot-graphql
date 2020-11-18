package scratches.boot.graphql.book.web;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import scratches.boot.graphql.book.domain.Book;
import scratches.boot.graphql.book.domain.BookRepository;

/**
 * @author Rashidi Zin
 */
@Component
@AllArgsConstructor
public class BookGraphQLQueryResolver implements GraphQLQueryResolver {

    private final BookRepository repository;

    public Iterable<Book> findAll() {
        return repository.findAll();
    }

    public Book findByTitle(String title) {
        return repository.findByTitle(title).orElse(null);
    }

}
