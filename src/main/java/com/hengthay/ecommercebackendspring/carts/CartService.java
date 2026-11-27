package com.hengthay.ecommercebackendspring.carts;

import com.hengthay.ecommercebackendspring.auth.AuthService;
import com.hengthay.ecommercebackendspring.products.InsufficientStockException;
import com.hengthay.ecommercebackendspring.products.ProductNotFoundException;
import com.hengthay.ecommercebackendspring.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final AuthService authService;

    public List<CartDto> getAllCarts() {
        var carts = cartRepository.findAll()
                .stream()
                .map(cartMapper::toDto)
                .toList();

        if(carts.isEmpty())
            throw new CartEmptyException("Cart is empty.");

        return carts;
    }

    public List<CartDto> getMyCarts() {
        // find user
        var user = authService.getCurrentUser();

        var carts =  cartRepository.findActiveCartByUserId(user.getId())
                .stream()
                .map(cartMapper::toDto)
                .toList();

        if(carts.isEmpty())
            throw new CartEmptyException("Cart is empty.");

        return carts;
    }

    @Transactional
    public CartItemDto addItemToCart(
            Long productId,
            Integer quantity
    ) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        var user = authService.getCurrentUser();

        // get existing cart or create new one
        var cart = cartRepository.findActiveCartByUserId(user.getId()).orElseGet(
                () -> {
                    var newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setActive(true);
                    return newCart;
                }
        );

        // find product
        var product = productRepository.findById(productId).orElseThrow(() ->
            new ProductNotFoundException("Product not found.")
        );

        // check existing product and stock
        CartItem existingItem = cart.getItem(productId);
        int currentQtyInCart = (existingItem != null) ? existingItem.getQuantity() : 0;
        int totalDesiredQty = currentQtyInCart + quantity;

        if (totalDesiredQty > product.getStock()) {
            throw new InsufficientStockException(
                    "Only " + product.getStock() + " item(s) available in stock for " + product.getTitle() + "."
            );
        }

        // add item to cartItem
        cart.addItem(product, quantity);

        // save to database
        var savedCart = cartRepository.save(cart);

        var savedItem = savedCart.getItem(productId);

        return cartMapper.toDto(savedItem);
    }

    public CartDto getCart(UUID cartId) {
        // get cart
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null)
            throw new CartNotFoundException("Cart not found.");

        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartItemDto updateCart(
            UUID cartId,
            Long productId,
            Integer quantity
    ) throws AccessDeniedException {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        var user = authService.getCurrentUser();
        // find cart
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null)
            throw new CartNotFoundException("Cart not found.");

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to deactivate this cart.");
        }

        // find product in cartItem
        var cartItem = cart.getItem(productId);
        if(cartItem == null)
            throw new ProductNotFoundException("Product not found.");

        // get product and check stock available before update quantity
        var product = cartItem.getProduct();
        if (quantity > product.getStock()) {
            throw new InsufficientStockException(
                    "Only " + product.getStock() + " item(s) available in stock for " + product.getTitle() + "."
            );
        }

        // update new quantity
        cartItem.setQuantity(quantity);
        // save to database
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeCart(UUID cartId, Long productId) {
        // find cart
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null)
            throw new CartNotFoundException("Cart not found.");
        // remove product from cartItem
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) throws AccessDeniedException {
        var user = authService.getCurrentUser();

        // find cart
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(
                () -> new CartNotFoundException("Cart not found.")
        );

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to deactivate this cart.");
        }

        cart.clearItems();
        cartRepository.save(cart);
    }

    @Transactional
    public void deactivateCart(UUID cartId) throws AccessDeniedException {
        var user = authService.getCurrentUser();
        // find cart
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null)
            throw new CartNotFoundException("Cart not found.");

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to deactivate this cart.");
        }

        cart.setActive(false);
        cartRepository.save(cart);
    }
}
