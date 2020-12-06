package scratches.boot.graphql.book.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static graphql.kickstart.execution.GraphQLRequest.createQueryOnlyRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Rashidi Zin
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "graphql.servlet.websocket.enabled=false")
public class BookRestAssuredClientTests {

    @LocalServerPort
    private int port;

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

        given()
                .port(port)
                .basePath("/graphql")
                .body(createQueryOnlyRequest(query))
                .post()
                .then()
                .assertThat()
                .root("data.findAll")
                .body("author.name", is(List.of("Rudyard Kipling", "Rudyard Kipling")))
                .body("title", is(List.of("If", "The Jungle Book")));
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

        given()
                .port(port)
                .basePath("/graphql")
                .body(createQueryOnlyRequest(query))
                .post()
                .then()
                .assertThat()
                .root("data.findByTitle")
                .body("author.name", is("Rudyard Kipling"))
                .body("title", is(nullValue()));
    }

}
