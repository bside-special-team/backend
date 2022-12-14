package com.beside.special.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PlaceRepository extends MongoRepository<Place, String> {
    List<Place> findByCoordinateBetweenAndPlaceType(Coordinate from, Coordinate to, PlaceType placeType);

    List<Place> findByCoordinateBetweenAndPlaceTypeOrderByRecommendCountDesc(Coordinate from, Coordinate to, PlaceType placeType);

    List<Place> findAllByPlaceType(PlaceType placeType);

    List<Place> findByWriter_IdAndCreatedAtBeforeOrderByCreatedAtDesc(String writerId, LocalDateTime createAt, Pageable pageable);
}
