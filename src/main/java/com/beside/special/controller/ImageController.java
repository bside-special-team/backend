package com.beside.special.controller;

import com.beside.special.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Image", description = "이미지 처리 관련")
@RequestMapping("/api/v1/images")
@RestController
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "이미지 등록 (multipart/form-data)", responses = {
            @ApiResponse(responseCode = "201", description = "이미지 등록 완료"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping
    public ResponseEntity<List<String> > uploadImage(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("dest") String dest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.uploadImage(images, dest));
    }

    // TODO WEBP 변환
    @Operation(summary = "이미지 다운로드", responses = {
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공 및 다운로드"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/{imageUUID}")
    public ResponseEntity<byte[]> resizeImage(@PathVariable String imageUUID) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageService.getImage(imageUUID).toByteArray());
    }
}