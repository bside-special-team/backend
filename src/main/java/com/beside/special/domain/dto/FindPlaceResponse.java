package com.beside.special.domain.dto;

import com.beside.special.domain.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(name = "플레이스 조회 결과")
@Getter
public class FindPlaceResponse {
    private List<Place> hiddenPlaces;
    private List<Place> landMarkPlaces;

    public FindPlaceResponse(List<Place> hiddenPlaces, List<Place> landMarkPlaces) {
        this.hiddenPlaces = hiddenPlaces;
        this.landMarkPlaces = landMarkPlaces;
    }
}
