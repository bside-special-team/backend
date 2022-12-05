package com.beside.special.service;

import com.beside.special.domain.*;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.exception.BadRequestException;
import com.beside.special.exception.NotFoundException;
import com.beside.special.service.dto.CreateBlockDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {
    private final BlockRepository blockRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    BlockService(PlaceRepository placeRepository, UserRepository userRepository, CommentRepository commentRepository, BlockRepository blockRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.blockRepository = blockRepository;
    }

    public BlockInfo create(UserDto user, CreateBlockDto createBlockDto) {
        User from = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 User"));

        blockRepository.findAllByUserIdAndType(from.getId(), createBlockDto.getType())
                .stream().filter(blockInfo -> blockInfo.getTargetId().equals(createBlockDto.getTargetId()))
                .findFirst()
                .ifPresent(
                        blockInfo -> {
                            throw new BadRequestException(String.format("이미 차단한 %s 입니다.", createBlockDto.getType().toString()));
                        }
                );

        String targetId = "";
        switch (createBlockDto.getType()) {
            case USER:
                User targetUser = userRepository.findById(createBlockDto.getTargetId())
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 User"));
                targetId = targetUser.getId();
                break;
            case PLACE:
                Place targetPlace = placeRepository.findById(createBlockDto.getTargetId())
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 Place"));
                targetId = targetPlace.getId();
                break;
            case COMMENT:
                Comment targetComment = commentRepository.findById(createBlockDto.getTargetId())
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 Comment"));
                targetId = targetComment.getId();
                break;
        }

        return blockRepository.save(BlockInfo.builder()
                .userId(from.getId())
                .targetId(targetId)
                .type(createBlockDto.getType())
                .build());
    }

    public void delete(UserDto user, CreateBlockDto createBlockDto) {
        BlockInfo blockInfo = blockRepository.findByUserIdAndTargetId(
                user.getUserId(), createBlockDto.getTargetId());

        blockRepository.delete(blockInfo);
    }

    public List<BlockInfo> findBlockInfo(UserDto user, BlockType blockType) {
        return blockRepository.findAllByUserIdAndType(user.getUserId(), blockType);
    }
}
