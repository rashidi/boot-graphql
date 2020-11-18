package scratches.boot.graphql.book.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Rashidi Zin
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class Author {

    @NonNull
    @Column("AUTHOR_NAME")
    private String name;

}
