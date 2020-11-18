package scratches.boot.graphql.book.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Embedded;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.data.relational.core.mapping.Embedded.OnEmpty.USE_NULL;

/**
 * @author Rashidi Zin
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE, onConstructor = @__(@PersistenceConstructor))
public class Book {

    @Id
    private Long id;

    @NonNull
    @Embedded(onEmpty = USE_NULL)
    private Author author;

    @NonNull
    private String title;

}
