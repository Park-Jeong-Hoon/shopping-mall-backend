package com.example.shop.repository;

import com.example.shop.model.Item;
import com.example.shop.model.ItemBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemBasketRepository extends JpaRepository<ItemBasket, Long> {

    @Query(value = "SELECT i.item FROM ItemBasket i WHERE i.member.id = :memberId")
    List<Item> getKeepItemsByMemberId(@Param(value = "memberId") Long memberId);

    List<ItemBasket> findAllByMember_IdAndItem_Id(Long memberId, Long itemId);

    void deleteByMember_IdAndItem_Id(Long memberId, Long itemId);
}
