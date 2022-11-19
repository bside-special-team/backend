package com.beside.special.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePlaceDto {
    private String name;
    private String placeId;
    private List<String> imageUuids;
    private List<String> hashTags;
}