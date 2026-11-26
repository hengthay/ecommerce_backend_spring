package com.hengthay.ecommercebackendspring.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "category")
    List<Product> findAll();

    @Override
    @EntityGraph(attributePaths = "category")
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAllByActiveTrue(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    List<Product> findByActiveTrue();
}