package com.beside.special.domain.dto;

import com.beside.special.domain.Coordinate;
import com.beside.special.domain.PlaceType;
import com.beside.special.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(name = "특정 플레이스 조회 결과")
@Builder
public class PlaceResponse {
    private String id;
    private User writer;
    private PlaceType placeType;
    private Coordinate coordinate;
    private String name;
    private List<String> imageUUIDs;
    private int visitCount;
    private int recCount;

}
