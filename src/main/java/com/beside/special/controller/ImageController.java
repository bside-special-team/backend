package com.beside.special.controller;

import com.beside.special.service.ImageUploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Image", description = "이미지 처리 관련")
@RequestMapping("/api/v1/images")
@RestController
public class ImageController {
    private final ImageUploader imageUploader;

    public ImageController(ImageUploader imageUploader) {
        this.imageUploader = imageUploader;
    }

    @Operation(summary = "이미지 등록 (multipart/form-data)", responses = {
            @ApiResponse(responseCode = "201", description = "이미지 등록 완료"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping
    public ResponseEntity uploadImage(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("dest") String dest) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageUploader.uploadImage(images, dest));
    }

    // TODO WEBP 변환 및 이미지 최적화
    @Operation(summary = "미구현 / 이미지 최적화 관련", responses = {
            @ApiResponse(responseCode = "201", description = "이미지 등록 완료"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/{imageUrl}")
    public byte[] resizeImage(@PathVariable String imageUrl){
        return null;
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body("image");
    }
}
