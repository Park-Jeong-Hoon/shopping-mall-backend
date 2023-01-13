package com.example.shop.repository;

import com.example.shop.dto.OrderDetailDto;
import com.example.shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(value = "SELECT new com.example.shop.dto.OrderDetailDto(o.order.id, o.order.orderStatus, o.order.price, o.item.name, o.item.price, o.quantity, o.order.orderDate) FROM OrderItem o WHERE o.order.id = :orderId")
    List<OrderDetailDto> getOrderDetailDtoListByOrderId(@Param(value = "orderId") Long orderId);
}
