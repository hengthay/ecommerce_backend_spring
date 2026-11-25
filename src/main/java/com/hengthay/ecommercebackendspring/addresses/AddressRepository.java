package com.hengthay.ecommercebackendspring.addresses;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Override
    @EntityGraph(attributePaths = "user")
    List<Address> findAll();

    @EntityGraph(attributePaths = "user")
    @Query("select a from Address a where a.active = true and a.id = :id")
    Optional<Address> findActiveTrueById(@Param("id") Long id);

    @EntityGraph(attributePaths = "user")
    @Query("select a from Address a where a.active = true and a.user.id = :userId")
    List<Address> findActiveTrueByUserId(@Param("userId") Long userId);
}