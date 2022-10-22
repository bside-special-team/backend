package com.beside.special.controller;

import com.beside.special.domain.Coordinate;
import com.beside.special.domain.Place;
import com.beside.special.domain.PlaceRepository;
import com.beside.special.domain.PlaceType;
import com.beside.special.domain.dto.FindPlaceResponse;
import com.beside.special.service.PlaceService;
import com.beside.special.service.dto.CreatePlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Place", description = "플레이스(명소)")
@RequestMapping("/api/v1/places")
@RestController
public class PlaceController {
    private final PlaceService placeService;

    private final PlaceRepository placeRepository;

    public PlaceController(PlaceService placeService, PlaceRepository placeRepository) {
        this.placeService = placeService;
        this.placeRepository = placeRepository;
    }

    @Operation(summary = "플레이스 전체 조회", responses = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping
    public FindPlaceResponse findAll() {
        List<Place> hiddenPlaces = placeRepository.findAllByPlaceType(PlaceType.HIDDEN);
        List<Place> landMarkPlaces = placeRepository.findAllByPlaceType(PlaceType.LAND_MARK);
        return new FindPlaceResponse(hiddenPlaces, landMarkPlaces);
    }

    @Operation(summary = "좌표 기반 플레이스 조회", responses = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("coordinate")
    public FindPlaceResponse findByCoordinate(
        @Parameter(description = "시작 위도", required = true, example = "120.12312312") BigDecimal fromLatitude,
        @Parameter(description = "시작 경도", required = true, example = "20.12312312") BigDecimal fromLongitude,
        @Parameter(description = "종료 위도", required = true, example = "124.12312312") BigDecimal toLatitude,
        @Parameter(description = "종료 경도", required = true, example = "28.12312312") BigDecimal toLongitude) {
        Coordinate from = new Coordinate(fromLatitude, fromLongitude);
        Coordinate to = new Coordinate(toLatitude, toLongitude);
        List<Place> hiddenPlaces = placeRepository.findByCoordinateBetweenAndPlaceType(from, to,
            PlaceType.HIDDEN);
        List<Place> landMarkPlaces = placeRepository.findByCoordinateBetweenAndPlaceType(from, to,
            PlaceType.LAND_MARK);
        return new FindPlaceResponse(hiddenPlaces, landMarkPlaces);
    }

    @Operation(summary = "히든 플레이스 등록", responses = {
        @ApiResponse(responseCode = "201", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Place create(@RequestBody @Valid CreatePlaceDto createPlaceDto) {
        return placeService.create(createPlaceDto);
    }
}
