package com.example.shop.model;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String region;
    private String road;
    private String home;
    private String zipcode;

    protected Address() {}

    public Address(String region, String road, String home, String zipcode) {
        this.region = region;
        this.road = road;
        this.home = home;
        this.zipcode = zipcode;
    }
}
