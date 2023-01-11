package com.example.shop.repository;

import com.example.shop.model.Item;
import com.example.shop.model.ItemBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemBasketRepository extends JpaRepository<ItemBasket, Long> {

    @Query(value = "SELECT i.item FROM ItemBasket i WHERE i.member.id = :memberId")
    List<Item> getKeepItemsByMemberId(@Param(value = "memberId") Long memberId);

    Optional<ItemBasket> findByItem_Id(Long itemId);

    void deleteByItem_Id(Long itemId);
}
