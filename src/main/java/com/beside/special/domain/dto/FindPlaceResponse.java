package com.beside.special.domain.dto;

import com.beside.special.domain.Place;
import lombok.Getter;

import java.util.List;

@Getter
public class FindPlaceResponse {
    private List<Place> hiddenPlaces;
    private List<Place> landMarkPlaces;

    public FindPlaceResponse(List<Place> hiddenPlaces, List<Place> landMarkPlaces) {
        this.hiddenPlaces = hiddenPlaces;
        this.landMarkPlaces = landMarkPlaces;
    }
}
