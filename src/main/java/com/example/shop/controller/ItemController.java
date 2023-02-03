package com.example.shop.controller;

import com.example.shop.auth.PrincipalDetails;
import com.example.shop.dto.ItemAddDto;
import com.example.shop.dto.ItemDto;
import com.example.shop.service.ItemService;
import com.example.shop.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;
    private final S3Service s3Service;

    public ItemController(ItemService itemService, S3Service s3Service) {
        this.itemService = itemService;
        this.s3Service = s3Service;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestPart("file") MultipartFile file, @RequestPart("json") ItemAddDto itemAddDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        String result = "success";

        try {
            String imgUrl = s3Service.uploadFile(file);
            itemService.add(itemAddDto, imgUrl, principalDetails.getMember().getId());
        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/keep")
    public ResponseEntity<String> keepOrder(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Long itemId) {

        String result = "success";

        try {
            itemService.keepOrderItem(principalDetails.getMember().getId(), itemId);
        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemDtoById(@PathVariable Long id) {

        ItemDto itemDto = itemService.getItemDtoById(id);

        return new ResponseEntity<>(itemDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemDto>> getItemDtoList() {

        List<ItemDto> itemDtoList = itemService.getItemDtoList();

        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @GetMapping("/all/{name}")
    public ResponseEntity<List<ItemDto>> getItemDtoListByNameContains(@PathVariable String name) {

        List<ItemDto> itemDtoList = itemService.getItemDtoListByNameContains(name);

        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @GetMapping("/basket")
    public ResponseEntity<List<ItemDto>> getItemDtoBasketList(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<ItemDto> itemDtoList = itemService.getItemDtoBasketByMemberId(principalDetails.getMember().getId());

        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @PostMapping("/basket/delete")
    public ResponseEntity<String> deleteItemBasketById(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Long itemId) {

        String result = "success";

        try {
            itemService.deleteItemBasketByMemberIdAndItemId(principalDetails.getMember().getId(), itemId);
        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/owns")
    public ResponseEntity<List<ItemDto>> getOwnItemDtoList(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<ItemDto> itemDtoList = itemService.getOwnItemDtoList(principalDetails.getMember().getId());

        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }
}
