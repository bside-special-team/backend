package com.beside.special.domain;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Getter
@Document
public class Place extends BaseEntity {
    private static final int MAX_NAME_LENGTH = 10;
    @Id
    private String id;

    private PlaceType placeType;

    private Category category;

    private Coordinate coordinate;

    private String name;

    private String description;

    private List<String> imageUrls;

    private Integer visitCount;

    private LocalTime bestStartTime;

    private LocalTime bestEndTime;

    private List<String> hashTags;

    private Season season;

    public Place(Category category, Coordinate coordinate, String name, String description, Integer visitCount,
                 List<String> imageUrls, LocalTime bestStartTime, LocalTime bestEndTime, List<String> hashTags,
                 Season season) {
        validate(category, coordinate, name, hashTags);
        //TODO 사진 validation
        this.placeType = PlaceType.HIDDEN;
        this.category = category;
        this.coordinate = coordinate;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
        this.visitCount = visitCount;
        this.bestStartTime = bestStartTime;
        this.bestEndTime = bestEndTime;
        this.hashTags = hashTags;
        this.season = season;
    }

    private static void validate(Category category, Coordinate coordinate, String name, List<String> hashTags) {
        Objects.requireNonNull(category);
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
