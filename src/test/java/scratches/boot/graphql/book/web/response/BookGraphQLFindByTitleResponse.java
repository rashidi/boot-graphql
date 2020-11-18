package scratches.boot.graphql.book.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import scratches.boot.graphql.book.domain.Book;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@Value
@Builder
@JsonDeserialize(builder = BookGraphQLFindByTitleResponse.BookGraphQLFindByTitleResponseBuilder.class)
public class BookGraphQLFindByTitleResponse {

    Data data;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BookGraphQLFindByTitleResponseBuilder { }

    @Value
    @Builder
    @JsonDeserialize(builder = Data.DataBuilder.class)
    public static class Data {

        @JsonProperty("findByTitle")
        Book content;

        @JsonPOJOBuilder(withPrefix = "")
        public static class DataBuilder { }

    }
}
