package com.beside.special.controller;

import com.beside.special.domain.Coordinate;
import com.beside.special.domain.Place;
import com.beside.special.domain.PlaceRepository;
import com.beside.special.domain.PlaceType;
import com.beside.special.domain.RecommendationResponse;
import com.beside.special.domain.dto.FindPlaceResponse;
import com.beside.special.service.PlaceService;
import com.beside.special.service.dto.CreatePlaceDto;
import com.beside.special.service.dto.VisitPlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        @ApiResponse(responseCode = "201", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "방문 내역이 존재하는 Place"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 ( User | Place ) 정보"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping
    public ResponseEntity<Place> create(@RequestBody @Valid CreatePlaceDto createPlaceDto) {
        Place place = placeService.create(createPlaceDto);
        placeService.visit(new VisitPlaceDto(createPlaceDto.getUserId(), place.getId()));

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(place);
    }

    @Operation(summary = "플레이스 방문", responses = {
        @ApiResponse(responseCode = "201", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "방문 내역이 존재하는 Place"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 ( User | Place ) 정보"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping("/check-in")
    public ResponseEntity<Place> visitPlace(@RequestBody VisitPlaceDto visitPlaceDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(placeService.visit(visitPlaceDto));
    }

    @Operation(summary = "플레이스 추천",
        description =
            "SUCCESS : 정상 추천 수 증가 완료" +
                "UPGRADE : 히든 플레이스 -> 랜드마크 승급 (해당 추천으로 인해)",
        responses = {
            @ApiResponse(responseCode = "201", description = "추천 완료 SUCCESS | UPGRADE"),
            @ApiResponse(responseCode = "400", description = "추천 내역이 존재하는 Place"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 ( User | Place ) 정보"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
        })
    @PostMapping("/recommendation")
    public ResponseEntity<RecommendationResponse> likePlace(@RequestBody VisitPlaceDto visitPlaceDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(placeService.recommend(visitPlaceDto));
    }
}
