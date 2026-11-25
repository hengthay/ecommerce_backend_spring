package com.hengthay.ecommercebackendspring.orders;

import com.hengthay.ecommercebackendspring.addresses.Address;
import com.hengthay.ecommercebackendspring.carts.Cart;
import com.hengthay.ecommercebackendspring.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders", schema = "ecommerce_backend_spring")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderItem> items = new LinkedHashSet<>();

    public static Order fromCartItem(Cart cart, User customer) {
        var order = new Order();
        order.setCustomer(customer);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setTotalAmount(cart.getTotalPrice());

        // loop through cart to get each item from cart
        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem(order, item.getProduct(), item.getQuantity());
            // add each item to order item
            order.items.add(orderItem);
        });

        return order;
    }

    public boolean isPlaced(User customer) {
        return this.customer.equals(customer);
    }
}
