package com.example.shop.service;

import com.example.shop.dto.ItemAddDto;
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

    public Long add(ItemAddDto itemAddDto, String imgUrl, Long memberId) throws Exception {

        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if(memberOptional.isEmpty()) {
            throw new Exception("존재하지 않는 회원");
        }
        Member member = memberOptional.get();

        Item item = new Item();
        item.setName(itemAddDto.getName());
        item.setPrice(itemAddDto.getPrice());
        item.setStockQuantity(itemAddDto.getStockQuantity());
        item.setImageName(imgUrl);
        item.setMember(member);
        itemRepository.save(item);

        return item.getId();
    }

    @Transactional
    public Long keepOrderItem(Long memberId, Long itemId) throws Exception {

        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if(memberOptional.isEmpty()) {
            throw new Exception("해당 회원 없음");
        }

        if(itemBasketRepository.findAllByMember_IdAndItem_Id(memberId, itemId).size() > 0) {
            throw new Exception("이미 장바구니에 추가된 아이템");
        }

        Item item = itemRepository.findById(itemId).get();

        ItemBasket itemBasket = new ItemBasket();
        itemBasket.setMember(memberOptional.get());
        itemBasket.setItem(item);
        itemBasketRepository.save(itemBasket);

        return itemBasket.getId();
    }

    public ItemDto getItemDtoById(Long id) {

        Optional<ItemDto> itemOptional = itemRepository.getItemDtoById(id);
        if (itemOptional.isEmpty()) {
            return null;
        }
        return itemOptional.get();
    }

    public List<ItemDto> getItemDtoList() {

        List<ItemDto> itemDtoList = itemRepository.getAllDtoList();

        return itemDtoList;
    }

    public List<ItemDto> getItemDtoListByNameContains(String name) {

        List<ItemDto> itemDtoList = itemRepository.getItemDtoListByNameContains(name);

        return itemDtoList;
    }

    public List<ItemDto> getItemDtoBasketByMemberId(Long memberId) {

        List<ItemDto> itemDtoList = itemBasketRepository.getKeepItemDtoListByMemberId(memberId);

        return itemDtoList;
    }

    @Transactional
    @Modifying
    public void deleteItemBasketByMemberIdAndItemId(Long memberId, Long itemId) throws Exception {

        if (itemBasketRepository.findAllByMember_IdAndItem_Id(memberId, itemId).size() == 0) {
            throw new Exception("장바구니 목록에 존재하지 않는 데이터");
        }

        itemBasketRepository.deleteByMember_IdAndItem_Id(memberId, itemId);
    }

    public List<ItemDto> getOwnItemDtoList(Long memberId) {

        List<ItemDto> itemDtoList = itemRepository.getOwnAllDtoList(memberId);

        return itemDtoList;
    }
}
