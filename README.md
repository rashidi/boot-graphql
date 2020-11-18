# GraphQL With Spring Boot
Implementing [GraphQL][1] server using [Spring Boot][2].

## Background
GraphQL provides the flexibility for clients to retrieve fields that are only relevant to them. In this tutorial we 
will explore how we can implement GraphQL server with Spring Boot.

## Dependencies
  - Java 15
  - graphql-java-tools
  - graphql-spring-boot-starter
  - graphiql-spring-boot-starter
  
Full list of dependencies can be found in [pom.xml][3].

## Implementation
We will start by implementing schema for [Book][5].

### [Book.graphqls][6]

```graphql
type Query {
    findAll: [Book]
    findByTitle(title: String): Book
}

type Author {
    name: String!
}

type Book {
    id: ID
    author: Author!
    title: String!
}
```

This is the schema that inform our clients on available fields and queries. Next is to implement a resolver.

### [BookGraphQLQueryResolver][7]
`BookGraphQLQueryResolver` will be the in-between GraphQL and repository. It will convert results return from 
[BookRepository][8] to what has been defined in [Book.graphqls][6].

```java
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
```

With that the implementation is complete.

## Verification
There are two verifications available in [BookRestClientTests][9]; [Find all books][10] and [Find one by title][11].

### Find All
In this test we will expect all Books will be listed in the response along with all of its information.

```java
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
}
```

Test above verifies that all books are available in response along with its author and title information. 

### Find one by title
In this test we only want a Book with title "If" to be returned, and we are only interested in its Author information.

```java
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "graphql.servlet.websocket.enabled=false")
class BookRestClientTests {

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders(toMultiValueMap(Map.of(CONTENT_TYPE, List.of("application/graphql"))));

    @Autowired
    private TestRestTemplate restTemplate;

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
```

We have verified that there is a book with title If and Rudyard Kipling is its author.

### Manual verification
To perform manual verification we will have to deploy the application and use [GraphiQL][12] to perform our queries.

#### Deploy the application
```shell script
mvnw spring-boot:start
```

Once the application is ready, GraphQL is accessible at [http://localhost:9000/graphql][14] and GraphiQL is available
at [http://localhost:9000/graphiql][13].

#### Find all Books
Provide the following content in the query panel:

```graphql
{
  findAll {
    id
    author {
      name
    }
    title                    
  }
}
```

The response will be as follows:

```json
{
  "data": {
    "findAll": [
      {
        "id": "1",
        "author": {
          "name": "Rudyard Kipling"
        },
        "title": "If"
      },
      {
        "id": "2",
        "author": {
          "name": "Rudyard Kipling"
        },
        "title": "The Jungle Book"
      }
    ]
  }
}
```

[1]: https://graphql.org/
[2]: https://spring.io/projects/spring-boot
[3]: pom.xml#L23
[5]: src/main/java/scratches/boot/graphql/book/domain/Book.java
[6]: src/main/resources/Book.graphqls
[7]: src/main/java/scratches/boot/graphql/book/web/BookGraphQLQueryResolver.java
[8]: src/main/java/scratches/boot/graphql/book/domain/BookRepository.java
[9]: src/test/java/scratches/boot/graphql/book/web/BookRestClientTests.java
[10]: src/test/java/scratches/boot/graphql/book/web/BookRestClientTests.java#L36
[11]: src/test/java/scratches/boot/graphql/book/web/BookRestClientTests.java#L61
[12]: https://github.com/graphql/graphiql
[13]: http://localhost:9000/graphiql
[14]: http://localhost:9000/graphql
