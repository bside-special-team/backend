package com.beside.special.service.dto;

import com.beside.special.domain.Place;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FindByCoordinatePlaceDto {
    private List<Place> hiddenPlaceList;
    private List<Place> randMarkList;

    private int hiddenPlaceCount;
    private int randMarkCount;
}
