package com.beside.special.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document
public class Comment extends BaseEntity {

    @Id
    private String id;

    private String comment;

    private User user;

    private Place place;

    public Comment(String comment, User user, Place place) {
        this.comment = comment;
        this.user = user;
        this.place = place;
    }
}
