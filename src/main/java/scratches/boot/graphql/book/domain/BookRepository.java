package scratches.boot.graphql.book.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

}
