package com.beside.special.controller;

import com.beside.special.domain.Coordinate;
import com.beside.special.domain.Place;
import com.beside.special.domain.PlaceRepository;
import com.beside.special.domain.PlaceType;
import com.beside.special.domain.dto.FindPlaceResponse;
import com.beside.special.service.PlaceService;
import com.beside.special.service.dto.CreatePlaceDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/v1/places")
@RestController
public class PlaceController {
    private final PlaceService placeService;

    private final PlaceRepository placeRepository;

    public PlaceController(PlaceService placeService, PlaceRepository placeRepository) {
        this.placeService = placeService;
        this.placeRepository = placeRepository;
    }

    @GetMapping
    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    @GetMapping("coordinate")
    public FindPlaceResponse findByCoordinate(BigDecimal fromLatitude, BigDecimal fromLongitude,
                                              BigDecimal toLatitude, BigDecimal toLongitude) {
        Coordinate from = new Coordinate(fromLatitude, fromLongitude);
        Coordinate to = new Coordinate(toLatitude, toLongitude);
        List<Place> hiddenPlaces = placeRepository.findByCoordinateBetweenAndPlaceType(from, to,
            PlaceType.HIDDEN);
        List<Place> landMarkPlaces = placeRepository.findByCoordinateBetweenAndPlaceType(from, to,
            PlaceType.LAND_MARK);
        return new FindPlaceResponse(hiddenPlaces, landMarkPlaces);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Place create(@RequestBody @Valid CreatePlaceDto createPlaceDto) {
        return placeService.create(createPlaceDto);
    }
}
