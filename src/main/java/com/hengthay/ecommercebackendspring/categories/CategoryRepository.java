package com.hengthay.ecommercebackendspring.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByActiveTrue();

    List<Category> findAllByActiveFalse();

    Optional<Category> findByName(String name);
}