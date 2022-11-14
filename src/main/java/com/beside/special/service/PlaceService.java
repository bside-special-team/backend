package com.beside.special.service;

import com.beside.special.domain.*;
import com.beside.special.exception.BadRequestException;
import com.beside.special.exception.NotFoundException;
import com.beside.special.service.dto.CreatePlaceDto;
import com.beside.special.service.dto.VisitPlaceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PlaceService {
    private static final int LAND_MARK_RECOMMENDATION = 2;

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Place create(CreatePlaceDto createPlaceDto) {
        User writer = userRepository.findById(createPlaceDto.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지않는 User"));

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
                .orElseThrow(() -> new NotFoundException("존재하지않는 Place"));

        User user = userRepository.findById(visitPlaceDto.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지않는 User"));

        for (VisitInfo userVisitPlace : user.getVisitInfos()) {
            if (userVisitPlace.getId().equals(place.getId())) {
                throw new BadRequestException("이전 방문 내역 [ " + userVisitPlace.getVisitedAt() + " ]");
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

    @Transactional
    public RecommendationResponse recommend(VisitPlaceDto visitPlaceDto) {
        Place place = placeRepository.findById(visitPlaceDto.getPlaceId())
                .orElseThrow(() -> new NotFoundException("존재하지않는 Place"));

        User user = userRepository.findById(visitPlaceDto.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지않는 User"));

        if (user.getRecPlaces().contains(place.getId())) {
            throw new BadRequestException("이미 추천한 장소입니다.");
        }

        user.getRecPlaces().add(place.getId());
        userRepository.save(user);

        place.getRecommendUsers().add(user.getId());
        if (place.getPlaceType() == PlaceType.HIDDEN && place.getRecommendUsers().size() >= LAND_MARK_RECOMMENDATION) {
            place.setPlaceType(PlaceType.LAND_MARK);
            placeRepository.save(place);
            return RecommendationResponse.UPGRADE;
        } else {
            placeRepository.save(place);
            return RecommendationResponse.SUCCESS;
        }
    }
}
