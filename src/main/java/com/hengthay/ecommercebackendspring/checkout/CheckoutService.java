package com.hengthay.ecommercebackendspring.checkout;

import com.hengthay.ecommercebackendspring.auth.AuthService;
import com.hengthay.ecommercebackendspring.carts.CartEmptyException;
import com.hengthay.ecommercebackendspring.carts.CartNotFoundException;
import com.hengthay.ecommercebackendspring.carts.CartRepository;
import com.hengthay.ecommercebackendspring.carts.CartService;
import com.hengthay.ecommercebackendspring.orders.Order;
import com.hengthay.ecommercebackendspring.orders.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@AllArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) throws AccessDeniedException {
        var cart = cartRepository.findById(request.getCartId()).orElse(null);

        if(cart == null)
            throw new CartNotFoundException("Cart not found.");

        if(cart.isEmpty())
            throw new CartEmptyException("Cart is empty.");

        var order = Order.fromCartItem(cart, authService.getCurrentUser());

        // set shipping address
        order.setAddressId(request.getAddressId());
        // save order
        orderRepository.save(order);

        // clear items in cart
        cartService.clearCart(request.getCartId());

        return new CheckoutResponse(order.getId(), "http://localhost:8080/api/products/success");
    }
}
