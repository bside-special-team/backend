package com.beside.special.service;

import com.beside.special.domain.Coordinate;
import com.beside.special.domain.Place;
import com.beside.special.domain.PlaceRepository;
import com.beside.special.domain.PlaceType;
import com.beside.special.domain.PointAction;
import com.beside.special.domain.RecommendationResponse;
import com.beside.special.domain.User;
import com.beside.special.domain.UserRepository;
import com.beside.special.domain.VisitInfo;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.exception.BadRequestException;
import com.beside.special.exception.ForbiddenException;
import com.beside.special.exception.NotFoundException;
import com.beside.special.service.dto.CreatePlaceDto;
import com.beside.special.service.dto.FindByCoordinatePlaceDto;
import com.beside.special.service.dto.GainPointResponse;
import com.beside.special.service.dto.UpdatePlaceDto;
import com.beside.special.service.dto.UserPointResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlaceService {
    private static final int LAND_MARK_RECOMMENDATION = 2;

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    private final UserPointCalculator userPointCalculator;

    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository,
                        UserPointCalculator userPointCalculator) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.userPointCalculator = userPointCalculator;
    }

    @Transactional
    public Place update(UserDto user, UpdatePlaceDto updatePlaceDto) {
        Place place = placeRepository.findById(updatePlaceDto.getPlaceId())
            .orElseThrow(() -> new NotFoundException("존재하지않는 Place"));

        if (!place.getWriter().getId().equals(user.getUserId())) {
            throw new ForbiddenException("수정 권한이 존재하지 않습니다.");
        }

        return placeRepository.save(place.update(updatePlaceDto));
    }

    @Transactional
    public void delete(UserDto user, String placeId) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("존재하지않는 Place"));

        if (!place.getWriter().getId().equals(user.getUserId())) {
            throw new ForbiddenException("삭제 권한이 존재하지 않습니다.");
        }

        placeRepository.delete(place);
    }

    @Transactional
    public GainPointResponse<Place> create(UserDto user, CreatePlaceDto createPlaceDto) {
        User writer = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new NotFoundException("존재하지않는 User"));

        Place place = new Place(
            createPlaceDto.getCoordinate(),
            createPlaceDto.getName(),
            writer,
            createPlaceDto.getImages(),
            createPlaceDto.getHashTags()
        );

        placeRepository.save(place);
        UserPointResponse userPointResponse = userPointCalculator.calculatePoint(writer, PointAction.CREATE_PLACE,
            place.getId());
        return new GainPointResponse(place, userPointResponse);
    }

    @Transactional
    public GainPointResponse<Place> visit(UserDto user, String placeId) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("존재하지않는 Place"));

        User writer = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new NotFoundException("존재하지않는 User"));

        place.getVisitInfos().stream()
            .filter(userVisitPlace -> userVisitPlace.getId().equals(place.getId()))
            .findFirst()
            .ifPresent(
                userVisitPlace -> {
                    throw new BadRequestException("이전 방문 내역 [ " + userVisitPlace.getVisitedAt() + " ]");
                }
            );

        LocalDateTime now = LocalDateTime.now();

        VisitInfo placeVisitInfo = VisitInfo.builder()
            .id(writer.getId())
            .visitedAt(now)
            .build();

        VisitInfo userVisitInfo = VisitInfo.builder()
            .id(place.getId())
            .visitedAt(now)
            .build();

        writer.getVisitInfos().add(userVisitInfo);
        place.getVisitInfos().add(placeVisitInfo);
        place.setVisitCount(place.getVisitCount() + 1);

        placeRepository.save(place);
        UserPointResponse userPointResponse = userPointCalculator.calculatePoint(writer, PointAction.VISIT,
            place.getId());
        userRepository.save(writer);

        return new GainPointResponse(place, userPointResponse);
    }

    @Transactional
    public RecommendationResponse recommend(UserDto user, String placeId) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException("존재하지않는 Place"));

        User recommender = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new NotFoundException("존재하지않는 User"));

        if (recommender.getRecPlaces().contains(place.getId())) {
            throw new BadRequestException("이미 추천한 장소입니다.");
        }

        recommender.getRecPlaces().add(place.getId());
        userRepository.save(recommender);

        place.getRecommendUsers().add(recommender.getId());
        place.setRecommendCount(place.getRecommendCount() + 1);

        if (place.getPlaceType() == PlaceType.HIDDEN && place.getRecommendUsers().size() >= LAND_MARK_RECOMMENDATION) {
            place.setPlaceType(PlaceType.LAND_MARK);
            placeRepository.save(place);
            return RecommendationResponse.UPGRADE;
        } else {
            placeRepository.save(place);
            return RecommendationResponse.SUCCESS;
        }
    }

    @Transactional
    public FindByCoordinatePlaceDto findByCoordinate(Coordinate from, Coordinate to) {
        List<Place> hiddenPlaceList =
            placeRepository.findByCoordinateBetweenAndPlaceTypeOrderByRecommendCountDesc(from, to, PlaceType.HIDDEN);
        List<Place> randMarkList = placeRepository.findByCoordinateBetweenAndPlaceTypeOrderByRecommendCountDesc(from,
            to, PlaceType.LAND_MARK);

        return FindByCoordinatePlaceDto.builder()
            .hiddenPlaceList(hiddenPlaceList.subList(0, Math.min((hiddenPlaceList.size()), 30)))
            .randMarkList(randMarkList.subList(0, Math.min((randMarkList.size()), 10)))
            .hiddenPlaceCount(hiddenPlaceList.size())
            .randMarkCount(randMarkList.size())
            .build();
    }

    public Place findById(String placeId) {
        return placeRepository.findById(placeId)
            .orElseThrow(() -> new NotFoundException(String.format("존재하지않는 Place [%s]", placeId)));
    }
}
