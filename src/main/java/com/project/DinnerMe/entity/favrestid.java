package com.project.DinnerMe.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class favrestid implements Serializable {

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private Client client;

    // Constructors, getters, setters, and other methods
}
