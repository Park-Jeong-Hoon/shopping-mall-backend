package com.example.shop;

import com.example.shop.dto.ItemAddDto;
import com.example.shop.dto.JoinDto;
import com.example.shop.model.Address;
import com.example.shop.repository.ItemBasketRepository;
import com.example.shop.service.ItemService;
import com.example.shop.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired MemberService memberService;
    @Autowired ItemService itemService;
    @Autowired ItemBasketRepository itemBasketRepository;

    @Test
    void add() throws Exception {

        // given
        Long memberId = createMember();
        ItemAddDto itemAddDto = makeItemAddDto();

        // when
        Long saveId = itemService.add(itemAddDto, "imgUrl", memberId);

        // then
        Assertions.assertEquals("computer", itemService.getItemDtoById(saveId).getName());
        Assertions.assertEquals("computer", itemService.getOwnItemDtoList(memberId).get(0).getName());
        Assertions.assertEquals("computer", itemService.getItemDtoListByNameContains("omp").get(0).getName());
    }

    @Test
    void keepOrderItem() throws Exception {

        // given
        Long memberId = createMember();
        ItemAddDto itemAddDto = makeItemAddDto();

        // when
        Long itemId = itemService.add(itemAddDto, "imgUrl", memberId);
        Long itemBasketId = itemService.keepOrderItem(memberId, itemId);

        // then
        Assertions.assertEquals(itemId, itemBasketRepository.findById(itemBasketId).get().getItem().getId());
    }

    @Test
    void deleteItemBasketByMemberIdAndItemId() throws Exception {

        // given
        Long memberId = createMember();
        ItemAddDto itemAddDto = makeItemAddDto();

        // when
        Long itemId = itemService.add(itemAddDto, "imgUrl", memberId);
        itemService.keepOrderItem(memberId, itemId);
        itemService.deleteItemBasketByMemberIdAndItemId(memberId, itemId);

        // then
        Assertions.assertEquals(0, itemService.getItemDtoBasketByMemberId(memberId).size());
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

    ItemAddDto makeItemAddDto() {

        ItemAddDto itemAddDto = new ItemAddDto();
        itemAddDto.setName("computer");
        itemAddDto.setPrice(1000000);
        itemAddDto.setStockQuantity(10);

        return itemAddDto;
    }
}
