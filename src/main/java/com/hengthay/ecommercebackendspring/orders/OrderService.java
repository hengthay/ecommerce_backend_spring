package com.hengthay.ecommercebackendspring.orders;

import com.hengthay.ecommercebackendspring.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;

    // get all orders
    public List<OrderDto> getAllOrders() {
        var order = orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();

        if(order.isEmpty())
            throw new OrderNotFoundException("Order not found.");

        return order;
    }

    // get own orders
    public List<OrderDto> getMyOrders() {
        var user = authService.getCurrentUser();

        var order = orderRepository.getOrderByCustomer(user.getId())
                .stream()
                .map(orderMapper::toDto)
                .toList();

        if(order.isEmpty())
            throw new OrderNotFoundException("Order not found.");

        return order;
    }

    // Get specific order with orderId
    public OrderDto getOrders(UUID orderId) throws AccessDeniedException {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(() -> {
            throw new OrderNotFoundException("Order not found.");
        });

        var user = authService.getCurrentUser();

        if(!order.isPlaced(user)) {
            throw new AccessDeniedException("Access Denied.");
        }

        return orderMapper.toDto(order);
    }

    // update status of order
    public OrderDto updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        // prevent change in case order is completed or cancelled
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of a " + order.getStatus() + " order.");
        }

        order.setStatus(newStatus);
        var savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }


}
