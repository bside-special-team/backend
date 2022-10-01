package com.beside.special.service;

import com.beside.special.domain.Category;
import com.beside.special.domain.Place;
import com.beside.special.domain.PlaceRepository;
import com.beside.special.service.dto.CreatePlaceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final CategoryService categoryService;

    public PlaceService(PlaceRepository placeRepository, CategoryService categoryService) {
        this.placeRepository = placeRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public Place create(CreatePlaceDto createPlaceDto) {
        Category category = categoryService.findByCode(createPlaceDto.getCategoryCode());
        //TODO Image update & Url 리스트 받아오기

        Place place = new Place(category,
            createPlaceDto.getCoordinate(),
            createPlaceDto.getName(),
            createPlaceDto.getDescription(),
            createPlaceDto.getVisitCount(),
            new ArrayList<>(),
            createPlaceDto.getBestStartTime(),
            createPlaceDto.getBestEndTime(),
            createPlaceDto.getHashTags(),
            createPlaceDto.getSeason()
            );

        return placeRepository.save(place);
    }
}
