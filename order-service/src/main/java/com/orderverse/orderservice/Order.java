package com.orderverse.orderservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "orders")
@Setter
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String productName;
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // pending, processing, shipped, delivered, cancelled

    // Getters and setters
}
