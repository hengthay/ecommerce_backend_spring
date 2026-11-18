package com.hengthay.ecommercebackendspring.categories;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getCategories() {
        var categories =  categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        if(categories.isEmpty()) {
            throw new CategoryNotFoundException("Category not found.");
        }

        return categories;
    }

    public List<CategoryDto> getAllCategories() {
        var categories =  categoryRepository.findAllByActiveTrue()
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        if(categories.isEmpty()) {
            throw new CategoryNotFoundException("Category not found.");
        }

        return categories;
    }

    public List<CategoryDto> getOnlyInActiveCategories() {
        var categories =  categoryRepository.findAllByActiveFalse()
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        if(categories.isEmpty()) {
            throw new CategoryNotFoundException("Category not found.");
        }

        return categories;
    }

    public CategoryDto getCategoryById(Long id) {
        var category = categoryRepository.findById(id).orElse(null);

        if(category == null)
            throw new CategoryNotFoundException("Category not found.");

        return categoryMapper.toDto(category);
    }

    public CategoryDto createCategory(
            CreateCategoryRequest request
    ) {
        var categoryEntity = categoryMapper.toEntity(request);
        categoryRepository.save(categoryEntity);

        return categoryMapper.toDto(categoryEntity);
    }

    public CategoryDto updateCategory(
            Long id,
            UpdateCategoryRequest request
    ) {
        var category = categoryRepository.findById(id).orElse(null);

        if(category == null)
            throw new CategoryNotFoundException("Category not found.");

        categoryMapper.update(request, category);

        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    public void deleteCategory(Long id) {
        var category = categoryRepository.findById(id).orElse(null);

        if(category == null)
            throw new CategoryNotFoundException("Category not found.");

        category.setActive(false);

        categoryRepository.save(category);
    }

    public void restoreCategory(Long id) {
        var category = categoryRepository.findById(id).orElse(null);

        if(category == null)
            throw new CategoryNotFoundException("Category not found.");

        category.setActive(true);

        categoryRepository.save(category);
    }
}
