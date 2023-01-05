package com.example.shop.controller;

import com.example.shop.auth.PrincipalDetails;
import com.example.shop.dto.OrderDetailDto;
import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderItemDto;
import com.example.shop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody List<OrderItemDto> orderItemDtoList) {

        String result = "success";

        try {
            orderService.saveOrder(principalDetails.getMember().getId(), orderItemDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getOrderDtoList(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<OrderDto> orderDtoList = orderService.getOrderDtoList(principalDetails.getMember().getId());

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDetailDto>> getOrderDetailDtoList(@PathVariable Long id) {

        List<OrderDetailDto> orderDetailDtoList = orderService.getOrderDetailDtoListById(id);

        return new ResponseEntity<>(orderDetailDtoList, HttpStatus.OK);
    }
}
