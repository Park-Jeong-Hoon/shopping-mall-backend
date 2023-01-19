package com.example.shop.dto;

import com.example.shop.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

    private Long orderId;
    private OrderStatus orderStatus;
    private int totalPrice;
    private String itemName;
    private int price;
    private int quantity;
    private String imageName;
    private LocalDateTime orderDate;
}
