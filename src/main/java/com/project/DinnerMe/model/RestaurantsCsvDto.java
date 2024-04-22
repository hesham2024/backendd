package com.project.DinnerMe.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RestaurantsCsvDto {
    private String name;
    private String address;
    private LocalTime open;
    private LocalTime close;
    private int tables;
    private String phoneNumber;
}