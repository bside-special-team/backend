package com.beside.special.service;

import com.beside.special.domain.CategoryRepository;
import com.beside.special.service.dto.CreateCategoryDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void 이미_존재하는_카테고리는_중복등록_불가() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto("code", "name");
        when(categoryRepository.existsByCode(any()))
            .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.create(createCategoryDto));
    }

    @Test
    void 카테고리를_정상적으로_등록한다() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto("code", "name");

        when(categoryRepository.existsByCode(any()))
            .thenReturn(false);


        assertDoesNotThrow(() -> categoryService.create(createCategoryDto));
    }
}
