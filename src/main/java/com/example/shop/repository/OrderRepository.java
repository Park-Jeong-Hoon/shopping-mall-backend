package com.example.shop.repository;

import com.example.shop.dto.OrderDto;
import com.example.shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT new com.example.shop.dto.OrderDto(o.id, o.orderStatus, o.price, o.orderDate, o.delivery.deliveryStatus) FROM Order o WHERE o.member.id = :memberId")
    List<OrderDto> getOrderDtoListByMemberId(@Param(value = "memberId") Long memberId);
}
