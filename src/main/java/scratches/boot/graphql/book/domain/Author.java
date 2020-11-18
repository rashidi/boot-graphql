package scratches.boot.graphql.book.domain;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.relational.core.mapping.Column;

/**
 * @author Rashidi Zin
 */
@Data
public class Author {

    @NonNull
    @Column("AUTHOR_NAME")
    private String name;

}
