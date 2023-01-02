package com.example.shop.service;

import com.example.shop.dto.ItemDto;
import com.example.shop.model.Item;
import com.example.shop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void add(ItemDto itemDto) {

        Item item = new Item();
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setStockQuantity(itemDto.getStockQuantity());
        itemRepository.save(item);
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
}
