package com.hengthay.ecommercebackendspring.categories;

import com.hengthay.ecommercebackendspring.config.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @GetMapping("/all-status")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories());
    }

    @RequestMapping("/in-active")
    public ResponseEntity<List<CategoryDto>> getOnlyInActiveCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getOnlyInActiveCategories());
    }

    @RequestMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreCategory(
            @PathVariable Long id
    ) {
        categoryService.restoreCategory(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCategoryNotFoundException(
            CategoryNotFoundException exception
    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto(
                        404,
                        exception.getMessage(),
                        null,
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(CategoryAccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(
            CategoryAccessDeniedException exception
    ) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDto(
                        404,
                        exception.getMessage(),
                        null,
                        Instant.now()
                )
        );
    }
}
