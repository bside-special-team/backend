package com.beside.special.service;

import com.beside.special.domain.*;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.exception.BadRequestException;
import com.beside.special.exception.ForbiddenException;
import com.beside.special.exception.NotFoundException;
import com.beside.special.service.dto.CreatePlaceDto;
import com.beside.special.service.dto.FindByCoordinatePlaceDto;
import com.beside.special.service.dto.GainPointResponse;
import com.beside.special.service.dto.UpdatePlaceDto;
import com.beside.special.service.dto.UserPointResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    private static final int LAND_MARK_RECOMMENDATION = 2;
    private static final int BLOCK_COUNT = 3;

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final UserPointCalculator userPointCalculator;
    private final BlockRepository blockRepository;


    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository,
                        UserPointCalculator userPointCalculator, BlockRepository blockRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.userPointCalculator = userPointCalculator;
        this.blockRepository = blockRepository;
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

        // 지울거
        User userDto = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 User"));

        if (place.getWriter().equals(userDto)) {
            System.out.println("equals 테스트");
        }

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
                createPlaceDto.getImageUuids(),
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
    public Place recommend(UserDto user, String placeId) {
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
            return placeRepository.save(place);
        } else {
            return placeRepository.save(place);
        }
    }

    @Transactional
    public void recommendUndo(UserDto user, String placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NotFoundException("존재하지않는 Place"));

        User recommender = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지않는 User"));

        if (!recommender.getRecPlaces().contains(place.getId())) {
            throw new BadRequestException("추천하지않은 장소입니다.");
        }

        place.getRecommendUsers().remove(recommender.getId());
        recommender.getRecPlaces().remove(place.getId());

        placeRepository.save(place);
        userRepository.save(recommender);
    }

    @Transactional(readOnly = true)
    public FindByCoordinatePlaceDto findByCoordinate(Coordinate from, Coordinate to) {
        int landMarkCount = placeRepository.findByCoordinateBetweenAndPlaceTypeOrderByRecommendCountDesc(from, to, PlaceType.LAND_MARK).size();
        int hiddenPlaceCount = placeRepository.findByCoordinateBetweenAndPlaceTypeOrderByRecommendCountDesc(from, to, PlaceType.HIDDEN).size();

        List<Place> landMarkList = placeRepository.findByCoordinateBetweenAndPlaceTypeOrderByRecommendCountDesc(from, to, PlaceType.LAND_MARK)
                .stream()
                .filter(place -> blockRepository.findAllByTargetId(place.getId()).size() < BLOCK_COUNT)
                .limit(10)
                .collect(Collectors.toList());

        List<Place> hiddenPlaceList = placeRepository.findByCoordinateBetweenAndPlaceTypeOrderByRecommendCountDesc(from, to, PlaceType.HIDDEN)
                .stream()
                .filter(place -> blockRepository.findAllByTargetId(place.getId()).size() < BLOCK_COUNT)
                .limit(30)
                .collect(Collectors.toList());

        return FindByCoordinatePlaceDto.builder()
                .hiddenPlaceList(hiddenPlaceList)
                .landMarkList(landMarkList)
                .hiddenPlaceCount(hiddenPlaceCount)
                .landMarkCount(landMarkCount)
                .build();
    }

    public Place findById(String placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new NotFoundException(String.format("존재하지않는 Place [%s]", placeId)));
    }

    public List<Place> recentVisited(UserDto user) {
        User visitor = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 User"));

        List<VisitInfo> visitInfos = visitor.getVisitInfos().stream()
                .sorted(Comparator.comparing(VisitInfo::getVisitedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());

        List<Place> places = new ArrayList<>();
        for (VisitInfo v : visitInfos) {
            placeRepository.findById(v.getId()).ifPresent(places::add);
        }
        return places;
    }

    public List<Place> writtenByMe(UserDto user, LocalDateTime lastCreateAt, int limit) {
        User writer = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 User"));
        Pageable page = PageRequest.of(0, limit);
        return placeRepository.findByWriter_IdAndCreatedAtBeforeOrderByCreatedAtDesc(writer.getId(), lastCreateAt, page);
    }

    public List<Place> recommendByMe(UserDto user) {
        User recommender = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 User"));

        List<Place> recommends = new ArrayList<>();

        for (String placeId : recommender.getRecPlaces()) {
            placeRepository.findById(placeId).ifPresent(recommends::add);
        }

        return recommends;
    }
}
