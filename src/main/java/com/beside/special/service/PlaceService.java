package com.beside.special.service;

import com.beside.special.domain.*;
import com.beside.special.service.dto.CreatePlaceDto;
import com.beside.special.service.dto.VisitPlaceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        placeRepository.save(place);

        return placeRepository.save(place);
    }

    @Transactional
    public Place visit(VisitPlaceDto visitPlaceDto) {
        Place place = placeRepository.findById(visitPlaceDto.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 Place"));

        User user = userRepository.findById(visitPlaceDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 User"));

        VisitInfo placeVisitInfo = VisitInfo.builder()
                .id(user.getId())
                .build();

        System.out.println(placeVisitInfo.getId() + " / " + placeVisitInfo.getVisitedAt());

        VisitInfo userVisitInfo = VisitInfo.builder()
                .id(place.getId())
                .build();

        System.out.println(userVisitInfo.getId() + " / " + userVisitInfo.getVisitedAt());

        if (user.getVisitInfos().contains(userVisitInfo) || place.getVisitInfos().contains(placeVisitInfo) ){
            throw new IllegalArgumentException("이미 방문한 장소입니다.");
        }

        user.getVisitInfos().add(userVisitInfo);
        place.getVisitInfos().add(placeVisitInfo);

        place.setVisitCount(place.getVisitCount() + 1);

        placeRepository.save(place);
        userRepository.save(user);

        return place;
    }
}
