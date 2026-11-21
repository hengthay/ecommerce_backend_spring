package com.hengthay.ecommercebackendspring.carts;

import com.hengthay.ecommercebackendspring.products.Product;
import com.hengthay.ecommercebackendspring.products.ProductNotFoundException;
import com.hengthay.ecommercebackendspring.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "carts", schema = "ecommerce_backend_spring")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new LinkedHashSet<>();

    public CartItem getItem(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product, int quantity) {
        // loop through item and check if existing we combined it quantity
        for(var existingItem : items) {
            if(existingItem.getProduct().getId().equals(product.getId())) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                return existingItem;
            }
        }

        // otherwise create new item
        CartItem newItem = new CartItem();
        newItem.setCart(this);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        items.add(newItem);

        return newItem;
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> {
            // check if product exists then remove cart
            if(item.getProduct().getId().equals(productId)) {
                item.setCart(null);
                return true;
            }
            // if not return false
            return false;
        });
    }

    // clear cart
    public void clearItems() {
        items.forEach(item -> item.setCart(null));
        items.clear();
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "UUID: " + id + "CartItem: " + items;
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found.")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Product not found in the cat.")
        );
    }
}
