package com.example.shop;

import com.example.shop.dto.ItemAddDto;
import com.example.shop.dto.JoinDto;
import com.example.shop.dto.OrderItemDto;
import com.example.shop.model.Address;
import com.example.shop.model.OrderStatus;
import com.example.shop.repository.OrderRepository;
import com.example.shop.service.ItemService;
import com.example.shop.service.MemberService;
import com.example.shop.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired MemberService memberService;
    @Autowired ItemService itemService;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void saveOrder() throws Exception {

        // given
        Long memberId = createMember();
        String itemName = "computer";
        List<OrderItemDto> orderItemDtoList = createOrderItemDtoList(memberId, itemName);

        // when
        Long orderId = orderService.saveOrder(memberId, orderItemDtoList);

        // then
        Assertions.assertEquals(memberId, orderRepository.findById(orderId).get().getMember().getId());
        Assertions.assertEquals(itemName, orderService.getOrderDetailDtoListById(orderId).get(0).getItemName());
    }

    @Test
    void cancelOrder() throws Exception {

        // given
        Long memberId = createMember();
        String itemName = "computer";
        List<OrderItemDto> orderItemDtoList = createOrderItemDtoList(memberId, itemName);

        // when
        Long orderId = orderService.saveOrder(memberId, orderItemDtoList);
        orderService.cancelOrder(orderId);

        // then
        Assertions.assertEquals(OrderStatus.CANCEL, orderService.getOrderDetailDtoListById(orderId).get(0).getOrderStatus());
    }

    Long createMember() {

        Address address = new Address("region1", "road1", "home1", "123-456");
        JoinDto joinDto = new JoinDto();
        joinDto.setUsername("test");
        joinDto.setPassword("1234");
        joinDto.setName("test");
        joinDto.setPhone("01012341234");
        joinDto.setEmail("abc@com");
        joinDto.setAddress(address);

        Long memberId = memberService.join(joinDto);

        return memberId;
    }

    List<OrderItemDto> createOrderItemDtoList(Long memberId, String itemName) throws Exception {

        List<OrderItemDto> orderItemDtoList = new ArrayList<>();

        ItemAddDto itemAddDto = new ItemAddDto();
        itemAddDto.setName(itemName);
        itemAddDto.setPrice(1000000);
        itemAddDto.setStockQuantity(100);

        Long itemId = itemService.add(itemAddDto, "imgUrl", memberId);

        orderItemDtoList.add(new OrderItemDto(itemId, 5));

        return orderItemDtoList;
    }
}
