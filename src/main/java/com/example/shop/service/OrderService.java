package com.example.shop.service;

import com.example.shop.dto.OrderDetailDto;
import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderItemDto;
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
    public Long saveOrder(Long memberId, List<OrderItemDto> orderItemDtoList) throws Exception {

        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if(memberOptional.isEmpty()) {
            throw new Exception("해당 회원 없음");
        }

        Member member = memberOptional.get();

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        int price = 0;
        String orderName = "";
        int count = 0;
        Order order = new Order();
        order.setMember(member);

        for (OrderItemDto orderItemDto : orderItemDtoList) {
            int quantity = orderItemDto.getQuantity();
            Item item = itemRepository.findById(orderItemDto.getId()).get();
            item.setStockQuantity(item.getStockQuantity() - quantity);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(quantity);
            orderItem.setOrderItemStatus(OrderItemStatus.ORDER);
            orderItemRepository.save(orderItem);

            int cost = item.getPrice() * quantity;
            Member itemSeller = item.getMember();
            itemSeller.setRevenue(itemSeller.getRevenue() + cost);
            price += cost;
            if (count == 0) {
                if (orderItemDtoList.size() == 1) {
                    orderName += item.getName() + " " + quantity + "개";
                } else {
                    orderName += item.getName() + " " + quantity + "개 외 " + (orderItemDtoList.size() - 1) + "종류";
                }
            }
            count++;
        }

        order.setPrice(price);
        order.setName(orderName);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setDelivery(delivery);
        orderRepository.save(order);

        return order.getId();
    }

    public List<OrderDto> getOrderDtoList(Long memberId) {

        return orderRepository.getOrderDtoListByMemberId(memberId);
    }

    public List<OrderDetailDto> getOrderDetailDtoListById(Long orderId) {

        return orderItemRepository.getOrderDetailDtoListByOrderId(orderId);
    }

    @Transactional
    @Modifying
    public void cancelOrder(Long orderId) throws Exception {

        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            throw new Exception("해당 주문건 없음");
        }

        Order order = orderOptional.get();

        if (order.getDelivery().getDeliveryStatus().equals(DeliveryStatus.START)) {
            throw new Exception("이미 배송이 시작된 주문");
        }

        order.setOrderStatus(OrderStatus.CANCEL);

        List<OrderItem> orderItemList = orderItemRepository.findAllByOrder_Id(orderId);

        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderItemStatus(OrderItemStatus.CANCEL);
            Item item = orderItem.getItem();
            item.setStockQuantity(item.getStockQuantity() + orderItem.getQuantity());

            Member itemSeller = item.getMember();
            itemSeller.setRevenue(itemSeller.getRevenue() - item.getPrice() * orderItem.getQuantity());
        }
    }
}
