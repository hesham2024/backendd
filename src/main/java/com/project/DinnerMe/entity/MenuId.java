package com.project.DinnerMe.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class MenuId implements Serializable {

    private Restaurant restaurant;
    private String category;
    private String foodName;
    private String typeOrSize;

    @Override
    public int hashCode() {
        return Objects.hash(restaurant, category, foodName, typeOrSize);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MenuId other = (MenuId) obj;

        return Objects.equals(restaurant, other.restaurant) &&
                Objects.equals(category, other.category) &&
                Objects.equals(foodName, other.foodName) &&
                Objects.equals(typeOrSize, other.typeOrSize);
    }
}