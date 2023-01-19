package com.example.shop.service;

import com.example.shop.dto.ItemDto;
import com.example.shop.model.Item;
import com.example.shop.model.ItemBasket;
import com.example.shop.model.Member;
import com.example.shop.repository.ItemBasketRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemBasketRepository itemBasketRepository;

    public ItemService(ItemRepository itemRepository, MemberRepository memberRepository, ItemBasketRepository itemBasketRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.itemBasketRepository = itemBasketRepository;
    }

    public void add(MultipartFile file, ItemDto itemDto) throws IOException {

        String filepath = "C:/shopfront/public/saveFolder/";
        String originFileName = file.getOriginalFilename();

        String saveFileName = System.currentTimeMillis() + originFileName;

        try {
            File file1 = new File(filepath + saveFileName);
            file.transferTo(file1);
        } catch (IOException e) {
            throw new IOException();
        }

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setStockQuantity(itemDto.getStockQuantity());
        item.setImageName(saveFileName);
        itemRepository.save(item);
    }

    @Transactional
    public Long keepOrderItem(Long memberId, Long itemId) throws Exception {

        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if(memberOptional.isEmpty()) {
            throw new Exception("해당 회원 없음");
        }

        Item item = itemRepository.findById(itemId).get();

        ItemBasket itemBasket = new ItemBasket();
        itemBasket.setMember(memberOptional.get());
        itemBasket.setItem(item);
        itemBasketRepository.save(itemBasket);

        return itemBasket.getId();
    }

    public Item getById(Long id) {

        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            return null;
        }
        return itemOptional.get();
    }

    public List<Item> getItemList() {

        List<Item> itemList = itemRepository.findAll();

        return itemList;
    }

    public List<Item> getItemListByNameContains(String name) {

        List<Item> itemList = itemRepository.findAllByNameContains(name);

        return itemList;
    }

    public List<Item> getItemBasketByMemberId(Long memberId) {

        List<Item> itemList = itemBasketRepository.getKeepItemsByMemberId(memberId);

        return itemList;
    }

    @Transactional
    @Modifying
    public void deleteItemBasketByItemId(Long itemId) throws Exception {

        Optional<ItemBasket> itemBasketOptional = itemBasketRepository.findByItem_Id(itemId);

        if (itemBasketOptional.isEmpty()) {
            throw new Exception("장바구니 목록에 존재하지 않는 데이터");
        }

        itemBasketRepository.deleteByItem_Id(itemId);
    }
}
