package com.example.shop.service;

import com.example.shop.model.*;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    @Modifying
    public Long saveOrder(Long memberId, List<Long> itemIdList) throws Exception {

        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if(memberOptional.isEmpty()) {
            throw new Exception("해당 회원 없음");
        }

        int price = 0;
        Order order = new Order();
        order.setMember(memberOptional.get());

        for (int i = 0; i < itemIdList.size(); i++) {
            Item item = itemRepository.findById(itemIdList.get(i)).get();
            item.setStockQuantity(item.getStockQuantity() - 1);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(1);
            orderItemRepository.save(orderItem);
            price += item.getPrice();
        }

        order.setPrice(price);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        return order.getId();
    }
}
