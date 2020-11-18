package scratches.boot.graphql.book.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import scratches.boot.graphql.book.domain.Author;
import scratches.boot.graphql.book.domain.Book;
import scratches.boot.graphql.book.web.response.BookGraphQLFindAllResponse;
import scratches.boot.graphql.book.web.response.BookGraphQLFindByTitleResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.CollectionUtils.toMultiValueMap;

/**
 * @author Rashidi Zin
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "graphql.servlet.websocket.enabled=false")
class BookRestClientTests {

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders(toMultiValueMap(Map.of(CONTENT_TYPE, List.of("application/graphql"))));

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Retrieve all books with full information")
    void findAll() {
        var query = """
                {
                  findAll {
                    id
                    author {
                      name
                    }
                    title                    
                  }
                }
                """;

        var response = restTemplate.postForEntity("/graphql", new HttpEntity<>(query, HTTP_HEADERS), BookGraphQLFindAllResponse.class);

        assertThat(response.getBody().getData().getContent())
                .usingElementComparatorOnFields("author.name", "title")
                .contains(
                        new Book(new Author("Rudyard Kipling"), "If"),
                        new Book(new Author("Rudyard Kipling"), "The Jungle Book")
                );
    }

    @Test
    @DisplayName("Retrieve a book by its title with only author name as response")
    void findByTitle() {
        var query = """
                {
                  findByTitle(title: "If") {
                    author {
                      name
                    }
                  }
                }
                """;

        var response = restTemplate.postForEntity("/graphql", new HttpEntity<>(query, HTTP_HEADERS), BookGraphQLFindByTitleResponse.class);

        assertThat(response.getBody().getData().getContent())
                .extracting(Book::getAuthor)
                .isEqualTo(new Author("Rudyard Kipling"));
    }
}
