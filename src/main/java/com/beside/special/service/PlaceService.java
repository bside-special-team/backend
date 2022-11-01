package com.beside.special.service;

import com.beside.special.domain.*;
import com.beside.special.service.dto.CreatePlaceDto;
import com.beside.special.service.dto.VisitPlaceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Place create(CreatePlaceDto createPlaceDto) {
        User writer = userRepository.findById(createPlaceDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 User"));

        Place place = new Place(
                createPlaceDto.getCoordinate(),
                createPlaceDto.getName(),
                writer,
                createPlaceDto.getImages(),
                createPlaceDto.getHashTags()
        );

        return placeRepository.save(place);
    }

    @Transactional
    public Place visit(VisitPlaceDto visitPlaceDto) {
        Place place = placeRepository.findById(visitPlaceDto.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 Place"));

        User user = userRepository.findById(visitPlaceDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 User"));

        for (VisitInfo userVisitPlace : user.getVisitInfos()) {
            if (userVisitPlace.getId().equals(place.getId())) {
                throw new IllegalArgumentException("이전 방문 내역 [ " +userVisitPlace.getVisitedAt() + " ]");
            }
        }

        LocalDateTime now = LocalDateTime.now();

        VisitInfo placeVisitInfo = VisitInfo.builder()
                .id(user.getId())
                .visitedAt(now)
                .build();

        VisitInfo userVisitInfo = VisitInfo.builder()
                .id(place.getId())
                .visitedAt(now)
                .build();

        user.getVisitInfos().add(userVisitInfo);
        place.getVisitInfos().add(placeVisitInfo);
        place.setVisitCount(place.getVisitCount() + 1);

        placeRepository.save(place);
        userRepository.save(user);

        return place;
    }
}
