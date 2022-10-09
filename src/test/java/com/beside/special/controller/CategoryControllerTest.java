package com.beside.special.controller;

import com.beside.special.domain.Category;
import com.beside.special.service.CategoryService;
import com.beside.special.service.dto.CreateCategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void 카테코리_정상_저장() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto("van", "van");

        when(categoryService.create(any()))
            .thenReturn(new Category("van", "van"));

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createCategoryDto)))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void 카테코리_code는_null일_수없다() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto(null, "van");

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createCategoryDto)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 카테코리_name은_null일_수없다() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto("van", null);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createCategoryDto)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }
}
