package guru.springframework.spring5webfluxrest.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Create by dmitri on 2022-11-20.
 */
@Data
@Document
public class Category {

    @Id
    private String id;

    private String description;
}
