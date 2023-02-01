package com.example.shop.repository;

import com.example.shop.dto.ItemDto;
import com.example.shop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT new com.example.shop.dto.ItemDto(i.id, i.name, i.price, i.stockQuantity, i.imageName, i.member.username) FROM Item i WHERE i.id = :id")
    Optional<ItemDto> getItemDtoById(@Param(value = "id") Long id);

    @Query(value = "SELECT new com.example.shop.dto.ItemDto(i.id, i.name, i.price, i.stockQuantity, i.imageName, i.member.username) FROM Item i")
    List<ItemDto> getAllDtoList();

    @Query(value = "SELECT new com.example.shop.dto.ItemDto(i.id, i.name, i.price, i.stockQuantity, i.imageName, i.member.username) FROM Item i " +
            "WHERE i.name LIKE %:name%")
    List<ItemDto> getItemDtoListByNameContains(@Param(value = "name") String name);

    @Query(value = "SELECT new com.example.shop.dto.ItemDto(i.id, i.name, i.price, i.stockQuantity, i.imageName, i.member.username) FROM Item i WHERE i.member.id = :memberId")
    List<ItemDto> getOwnAllDtoList(@Param(value = "memberId") Long memberId);
}
