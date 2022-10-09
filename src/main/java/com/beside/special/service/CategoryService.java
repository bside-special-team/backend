package com.beside.special.service;

import com.beside.special.domain.Category;
import com.beside.special.domain.CategoryRepository;
import com.beside.special.service.dto.CreateCategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Category findByCode(String code) {
        return categoryRepository.findCategoryByCode(code)
            .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 category code입니다. %s", code)));
    }

    @Transactional
    public Category create(CreateCategoryDto createCategoryDto) {
        if (categoryRepository.existsByCode(createCategoryDto.getCode())) {
            throw new IllegalArgumentException(String.format("이미 존재하는 category code입니다. %s",
                createCategoryDto.getCode()));
        }
        Category category = new Category(createCategoryDto.getCode(), createCategoryDto.getName());

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
