package com.beside.special.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("image")
@Getter
@Setter
@Builder
public class Image extends BaseEntity {
    @Id
    private String id;
    private String uuid;
    private String fileKey;
    private String userId;
}
