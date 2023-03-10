package com.example.shop.dto;

import com.example.shop.model.DeliveryStatus;
import com.example.shop.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private OrderStatus orderStatus;
    private int price;
    private String name;
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;
}
