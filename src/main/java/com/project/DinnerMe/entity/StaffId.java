package com.project.DinnerMe.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class StaffId implements Serializable {

    private Long id;
    private Long restaurant;

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurant);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StaffId staffId = (StaffId) obj;
        return Objects.equals(id, staffId.id) &&
                Objects.equals(restaurant, staffId.restaurant);
    }
}
