package com.project.DinnerMe.entity;

import java.io.Serializable;
import java.util.Objects;

public class TablesId implements Serializable {

    private Long restaurant; // Change to Long type (restaurant ID)
    private Long id;

    // Constructors, getters, setters

    // Implement hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(restaurant, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TablesId tablesId = (TablesId) obj;
        return Objects.equals(restaurant, tablesId.restaurant) &&
                Objects.equals(id, tablesId.id);
    }
}
