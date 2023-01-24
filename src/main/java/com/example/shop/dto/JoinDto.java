package com.example.shop.dto;

import com.example.shop.model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinDto {

    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private Address address;
}
