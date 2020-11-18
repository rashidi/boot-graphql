package scratches.boot.graphql.book.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@DataJdbcTest
class BookRepositoryTests {

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void insert() {
        repository.save(new Book(new Author("Rudyard Kipling"), "Jungle Book"));
    }

    @Test
    void findByTitle() {
        assertThat(repository.findByTitle("Jungle Book")).isNotEmpty();
    }
    
}
