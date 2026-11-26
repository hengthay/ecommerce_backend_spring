package com.hengthay.ecommercebackendspring.products;

import com.hengthay.ecommercebackendspring.config.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductPageResponse> getProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortDir
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProducts(page, size, sortDir));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductDto>> getActiveProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getActiveProducts());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> createProduct(
            @Valid @ModelAttribute CreateProductRequest request,
            @RequestPart(value = "imageUrl") MultipartFile imageUrl
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request, imageUrl)
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateProductRequest request,
            @RequestPart(value = "imageUrl") MultipartFile imageUrl
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(id, request, imageUrl));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreProduct(@PathVariable Long id) {
        productService.restoreProduct(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorDto(
                                404,
                                ex.getMessage(),
                                null,
                                Instant.now()
                        )
                );
    }

    @ExceptionHandler(ProductAccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleProductAccessDeniedException(ProductAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorDto(
                                404,
                                ex.getMessage(),
                                null,
                                Instant.now()
                        )
                );
    }
}
