package com.beside.special.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlaceRepository extends MongoRepository<Place, String> {
    List<Place> findByCoordinateBetweenAndPlaceType(Coordinate from, Coordinate to, PlaceType placeType);

    List<Place> findAllByPlaceType(PlaceType placeType);
}
