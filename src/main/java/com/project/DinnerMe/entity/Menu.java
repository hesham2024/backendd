package com.project.DinnerMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.TimeZoneColumn;

import java.time.LocalTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(MenuId.class)
public class Menu {
    @Id
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    @Id
    private String category;
    @Id
    private String foodName;

    @Id
    private String typeOrSize=" ";

    @NotNull
    private Number price;


}