package com.beside.special.controller;

import com.beside.special.domain.AuthUser;
import com.beside.special.domain.BlockInfo;
import com.beside.special.domain.BlockType;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.service.BlockService;
import com.beside.special.service.dto.CreateBlockDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Block", description = "차단, 신고 관리")
@RequestMapping("/api/v1/block")
@RestController
public class BlockController {
    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @GetMapping("/")
    public ResponseEntity<List<BlockInfo>> get(@Parameter(hidden = true) @AuthUser UserDto user,
                                               @RequestParam BlockType blockType) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(blockService.findBlockInfo(user, blockType));
    }

    @PostMapping("/")
    public ResponseEntity<BlockInfo> insert(@Parameter(hidden = true) @AuthUser UserDto user,
                                            @RequestBody CreateBlockDto createBlockDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(blockService.create(user, createBlockDto));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> delete(@Parameter(hidden = true) @AuthUser UserDto user,
                                    @RequestBody CreateBlockDto createBlockDto) {
        blockService.delete(user, createBlockDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
