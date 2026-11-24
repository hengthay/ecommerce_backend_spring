package com.hengthay.ecommercebackendspring.orders;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Override
    @EntityGraph(attributePaths = {"items.product", "customer"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"items.product", "customer"})
    @Query("select o from Order o where o.customer.id = :userId")
    List<Order> getOrderByCustomer(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"items.product", "customer"})
    @Query("select o from Order o where o.id = :orderId")
    Optional<Order> getOrderWithItems(@Param("orderId") UUID orderId);
}