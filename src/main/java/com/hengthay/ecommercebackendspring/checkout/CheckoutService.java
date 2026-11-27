package com.hengthay.ecommercebackendspring.checkout;

import com.hengthay.ecommercebackendspring.addresses.AddressNotFoundException;
import com.hengthay.ecommercebackendspring.addresses.AddressRepository;
import com.hengthay.ecommercebackendspring.auth.AuthService;
import com.hengthay.ecommercebackendspring.carts.*;
import com.hengthay.ecommercebackendspring.orders.Order;
import com.hengthay.ecommercebackendspring.orders.OrderRepository;
import com.hengthay.ecommercebackendspring.products.InsufficientStockException;
import com.hengthay.ecommercebackendspring.products.ProductNotFoundException;
import com.hengthay.ecommercebackendspring.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws AccessDeniedException {
        var currentUser = authService.getCurrentUser();

        var cart = cartRepository.findById(request.getCartId()).orElseThrow(() ->
            new CartNotFoundException("Cart not found.")
        );

        if (!cart.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to checkout this cart.");
        }

        if(cart.isEmpty())
            throw new CartEmptyException("Cart is empty.");

        var order = Order.fromCartItem(cart, currentUser);

        var address = addressRepository.findActiveTrueById(request.getAddressId()).orElseThrow(() ->
            new AddressNotFoundException("Address not found.")
        );

        // To prevent race condition and deducted product stock
        List<CartItem> sortedItem = cart.getItems().stream()
                .sorted(Comparator.comparing(item -> item.getProduct().getId()))
                .toList();

        // loop through each product and update it quantity
        for(CartItem item : sortedItem) {
            int quantity = item.getQuantity();

            var product = productRepository.findByIdForUpdate(item.getProduct().getId()).orElseThrow(
                    () -> new ProductNotFoundException("Product not found.")
            );

            if(product.getStock() < quantity) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getTitle()
                );
            }

            product.setStock(product.getStock() - quantity);
        }
        // set shipping address
        order.setAddress(address);
        // save order
        orderRepository.save(order);

        // clear items in cart
        cartService.clearCart(request.getCartId());

        return new CheckoutResponse(order.getId(), "http://localhost:8080/api/products/success");
    }
}
