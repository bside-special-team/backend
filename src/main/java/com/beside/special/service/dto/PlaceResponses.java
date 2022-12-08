package com.beside.special.service.dto;

import com.beside.special.domain.Place;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
public class PlaceResponses {
    private final List<Place> places;
    private final long lastTimestamp;
    private final boolean hasNext;

    public PlaceResponses(List<Place> places, long lastTimestamp, boolean hasNext) {
        this.places = places;
        this.lastTimestamp = lastTimestamp;
        this.hasNext = hasNext;
    }

    public static PlaceResponses from(List<Place> placeResponses, int limit) {
        Long timestamp = placeResponses.stream()
                .map(Place::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .map(it -> it.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli())
                .orElse(0L);
        return new PlaceResponses(placeResponses, timestamp, placeResponses.size() == limit);
    }
}