package com.project.DinnerMe.model;

import com.project.DinnerMe.entity.Restaurant;
import lombok.Data;

@Data
public class MenuCsvDto {
    private Restaurant rest;
    private String category;
    private String foodName;
    private String typeOrSize;
    private Number price;
}