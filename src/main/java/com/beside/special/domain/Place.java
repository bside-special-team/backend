package com.beside.special.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Document
public class Place extends BaseEntity {
    private static final int MAX_NAME_LENGTH = 10;
    @Id
    private String id;

    private User writer;

    private PlaceType placeType;

    private Coordinate coordinate;

    private String name;

    private List<String> imageUrls;

    private Integer visitCount = 1;

    private List<String> hashTags;

    private Set<VisitInfo> visitInfos;

    public Place(Coordinate coordinate, String name,  User writer,
                 List<String> imageUrls, List<String> hashTags) {
        // validate(coordinate, name, hashTags);
        // TODO 사진 validation
        this.placeType = PlaceType.HIDDEN;
        this.coordinate = coordinate;
        this.name = name;
        this.writer = writer;
        this.imageUrls = imageUrls;
        this.hashTags = hashTags;
    }

    private static void validate(Category category, Coordinate coordinate, String name, List<String> hashTags) {
        // Objects.requireNonNull(category);
        Objects.requireNonNull(coordinate);
        Objects.requireNonNull(name);

        if (Strings.isBlank(name) || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("명소명은 필수이며 10자 이하여야합니다.");
        }

        if (!CollectionUtils.isEmpty(hashTags) && hashTags.size() > 3) {
            throw new IllegalArgumentException("해시태그는 최대 3개 등록 가능합니다.");
        }
    }
}
