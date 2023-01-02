package com.example.shop.controller;

import com.example.shop.dto.ItemDto;
import com.example.shop.model.Item;
import com.example.shop.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody ItemDto itemDto) {

        String result = "success";

        try {
            itemService.add(itemDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {

        Item item = itemService.getById(id);

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getItemList() {

        List<Item> itemList = itemService.getItemList();

        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }
}
