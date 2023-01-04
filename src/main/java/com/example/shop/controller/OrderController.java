package com.example.shop.controller;

import com.example.shop.auth.PrincipalDetails;
import com.example.shop.dto.OrderDto;
import com.example.shop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody List<OrderDto> orderDtoList) {

        String result = "success";

        try {
            orderService.saveOrder(principalDetails.getMember().getId(), orderDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
