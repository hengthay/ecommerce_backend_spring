package com.hengthay.ecommercebackendspring.carts;

import com.hengthay.ecommercebackendspring.config.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(
                cartService.getAllCarts()
        );
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        return ResponseEntity.ok(
                cartService.getCart(cartId)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<CartDto>> getMyCarts() {
        return ResponseEntity.ok(cartService.getMyCarts());
    }

    @PostMapping("/me/items")
    public ResponseEntity<CartItemDto> addItemToCart(@Valid @RequestBody AddItemToCartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItemToCart(
                        request.getProductId(), request.getQuantity())
                );
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateItemFromCart(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartRequest request) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(cartService.updateCart(
                        cartId, productId, request.getQuantity())
                );
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable UUID cartId,
            @PathVariable Long productId)
    {
        cartService.removeCart(cartId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) throws AccessDeniedException {
        cartService.clearCart(cartId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deactivateCart(@PathVariable UUID cartId) throws AccessDeniedException {
        cartService.deactivateCart(cartId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFoundException(
            CartNotFoundException exception
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

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ErrorDto> handleCartEmptyException(
            CartEmptyException exception
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(
            AccessDeniedException exception
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDto(
                        403,
                        exception.getMessage(),
                        null,
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDto(
                        400,
                        "Invalid format for parameter '" + exception.getName() + "': " + exception.getValue(),
                        null,
                        Instant.now()
                )
        );
    }
}
