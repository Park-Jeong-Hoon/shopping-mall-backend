package com.example.shop.repository;

import com.example.shop.dto.ItemDto;
import com.example.shop.model.ItemBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemBasketRepository extends JpaRepository<ItemBasket, Long> {

    @Query(value = "SELECT new com.example.shop.dto.ItemDto(i.item.id, i.item.name, i.item.price, i.item.stockQuantity, i.item.imageName, i.item.member.username) FROM ItemBasket i WHERE i.member.id = :memberId")
    List<ItemDto> getKeepItemDtoListByMemberId(@Param(value = "memberId") Long memberId);

    List<ItemBasket> findAllByMember_IdAndItem_Id(Long memberId, Long itemId);

    void deleteByMember_IdAndItem_Id(Long memberId, Long itemId);
}
