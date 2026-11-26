package com.hengthay.ecommercebackendspring.products;

import com.hengthay.ecommercebackendspring.categories.CategoryNotFoundException;
import com.hengthay.ecommercebackendspring.categories.CategoryRepository;
import com.hengthay.ecommercebackendspring.services.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileStorageService fileStorageService;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> getAllProducts() {
        var products =  productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();

        if(products.isEmpty())
            throw new ProductNotFoundException("Product not found.");

        return products;
    }

    public List<ProductDto> getActiveProducts() {
        var products = productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toDto)
                .toList();

        if(products.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }

        return products;
    }


    public ProductPageResponse getProducts(int page, int size, String sortDir) {
        int safeSize = Math.clamp(size, 1, 100);
        int safePage = Math.max(page, 0);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by("id").ascending()
                    : Sort.by("id").descending();

        Pageable pageable = PageRequest.of(safePage, safeSize, sort);

        Page<ProductDto> productPage = productRepository
                .findAllByActiveTrue(pageable)
                .map(productMapper::toDto);

        if(productPage.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }

        return ProductPageResponse.from(productPage);
    }

    public ProductDto getProductById(Long id) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found."));

        return productMapper.toDto(product);
    }

    @Transactional
    public ProductDto createProduct(
            CreateProductRequest request, MultipartFile imageUrl
    ) {

        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);

        if(category == null)
            throw new CategoryNotFoundException("Category not found.");

        // store image in /uploads/products folder
        String imagePath = fileStorageService.storeImageFile(imageUrl, "products");

        var productEntity = productMapper.toEntity(request);
        productEntity.setImageUrl(imagePath);
        productEntity.setCategory(category);

        productRepository.save(productEntity);

        return productMapper.toDto(productEntity);
    }

    public ProductDto updateProduct(
            Long id,
            UpdateProductRequest request,
            MultipartFile imageUrl
    ) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));

        // check image and replace new one if exist
        if(imageUrl != null && !imageUrl.isEmpty()) {
            String newImagePath = fileStorageService.storeImageFile(imageUrl, "products");
            fileStorageService.deleteFile(product.getImageUrl());
            product.setImageUrl(newImagePath);
        }

        // update new product, set new category
        productMapper.update(request, product);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public void deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        product.setActive(false);
        productRepository.save(product);
    }

    public void restoreProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        product.setActive(true);
        productRepository.save(product);
    }
}
