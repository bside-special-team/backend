package com.beside.special.controller;

import com.beside.special.domain.*;
import com.beside.special.domain.dto.FindPlaceResponse;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.service.PlaceService;
import com.beside.special.service.dto.CreatePlaceDto;
import com.beside.special.service.dto.FindByCoordinatePlaceDto;
import com.beside.special.service.dto.GainPointResponse;
import com.beside.special.service.dto.UpdatePlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "히든 플레이스 등록", responses = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 ( User ) 정보"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping
    public ResponseEntity<GainPointResponse<Place>> create(@RequestBody CreatePlaceDto createPlaceDto,
                                                           @Parameter(hidden = true) @AuthUser UserDto user) {
        GainPointResponse<Place> place = placeService.create(user, createPlaceDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(place);
    }

    @Operation(summary = "플레이스 수정", responses = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "Place 작성자가 아님"),
            @ApiResponse(responseCode = "404", description = "존재하지않는 Place"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PutMapping
    public ResponseEntity<Place> update(
            @RequestBody UpdatePlaceDto updatePlaceDto,
            @Parameter(hidden = true) @AuthUser UserDto user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(placeService.update(user, updatePlaceDto));
    }

    @Operation(summary = "플레이스 삭제", responses = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "Place 작성자가 아님"),
            @ApiResponse(responseCode = "404", description = "존재하지않는 Place"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @DeleteMapping("/")
    public ResponseEntity<?> delete(
            @RequestParam String placeId,
            @Parameter(hidden = true) @AuthUser UserDto user) {
        placeService.delete(user, placeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "좌표 기반 플레이스 조회", responses = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/coordinate")
    public ResponseEntity<FindByCoordinatePlaceDto> findByCoordinate(
            @Parameter(description = "시작 위도", required = true, example = "120.12312312") BigDecimal fromLatitude,
            @Parameter(description = "시작 경도", required = true, example = "20.12312312") BigDecimal fromLongitude,
            @Parameter(description = "종료 위도", required = true, example = "124.12312312") BigDecimal toLatitude,
            @Parameter(description = "종료 경도", required = true, example = "28.12312312") BigDecimal toLongitude) {
        Coordinate from = new Coordinate(fromLatitude, fromLongitude);
        Coordinate to = new Coordinate(toLatitude, toLongitude);

        return ResponseEntity.status(HttpStatus.OK)
                .body(placeService.findByCoordinate(from, to));
    }

    @Operation(summary = "플레이스 방문", responses = {
        @ApiResponse(responseCode = "201", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "방문 내역이 존재하는 Place"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 ( User | Place ) 정보"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping("/check-in")
    public ResponseEntity<GainPointResponse<Place>> visitPlace(@RequestParam String placeId,
                                                               @Parameter(hidden = true) @AuthUser UserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(placeService.visit(user, placeId));
    }

    @Operation(summary = "플레이스 추천",
            description =
                    "SUCCESS : 정상 추천 수 증가 완료\n" + "UPGRADE : 히든 플레이스 -> 랜드마크 승급 (해당 추천으로 인해)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "추천 완료 SUCCESS | UPGRADE"),
                    @ApiResponse(responseCode = "400", description = "추천 내역이 존재하는 Place"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 ( User | Place ) 정보"),
                    @ApiResponse(responseCode = "500", description = "서버 에러")
            })

    @PostMapping("/recommendation")
    public ResponseEntity<RecommendationResponse> likePlace(@RequestParam String placeId,
                                                            @Parameter(hidden = true) @AuthUser UserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(placeService.recommend(user, placeId));
    }
}
