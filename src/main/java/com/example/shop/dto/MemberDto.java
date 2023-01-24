package com.example.shop.dto;

import com.example.shop.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String username;
    private String name;
    private LocalDateTime joinDate;
    private String phone;
    private String email;
    private Address address;
}
