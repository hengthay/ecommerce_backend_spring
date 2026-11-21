package com.hengthay.ecommercebackendspring.carts;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    // To perform join items in cartItem then product to avoid N+1
    @EntityGraph(attributePaths = "items.product")
    @Query("select c from Cart c where c.user.id = :userId and c.active = true")
    Optional<Cart> findActiveCartByUserId(@Param("userId") Long userId);

    // To perform join items in cartItem then product to avoid N+1
    @EntityGraph(attributePaths = "items.product")
    @Query("select c from Cart c where c.id = :cartId")
    Optional<Cart> getCartWithItems(@Param("cartId") UUID cartId);
}