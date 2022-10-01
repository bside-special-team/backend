package com.beside.special.controller;

import com.beside.special.domain.Category;
import com.beside.special.service.CategoryService;
import com.beside.special.service.dto.CreateCategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Category", description = "카테고리")
@RequestMapping("/api/v1/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 전체 조회", responses = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @Operation(summary = "카테고리 등록", responses = {
        @ApiResponse(responseCode = "201", description = "등록 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return categoryService.create(createCategoryDto);
    }
}
