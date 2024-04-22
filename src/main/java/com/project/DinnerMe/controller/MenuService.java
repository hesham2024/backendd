package com.project.DinnerMe.controller;

import com.project.DinnerMe.entity.Menu;
import com.project.DinnerMe.entity.MenuId;
import com.project.DinnerMe.entity.Restaurant;
import com.project.DinnerMe.model.MenuCsvDto;
import com.project.DinnerMe.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    @Autowired
    MenuRepository mr;
    public void addMenuItem(MenuCsvDto menuItemDto, Restaurant r) {


// Create a Menu
        Menu menu = new Menu();
        menu.setRestaurant(r); // Set the restaurant
        menu.setCategory(menuItemDto.getCategory()); // Set the category
        menu.setFoodName(menuItemDto.getFoodName()); // Set the food name
        menu.setTypeOrSize(menuItemDto.getTypeOrSize()); // Set the type or size
        menu.setPrice(menuItemDto.getPrice()); // Set other attributes

// Save the Menu entity
        mr.save(menu);
    }

    public void deleteMenuItem(MenuId menuItemId) {
        mr.deleteById(menuItemId);
    }

    public void updateMenuItem(MenuId menuItemId, MenuCsvDto menuItemDto) {
        // Step 1: Retrieve the existing Menu entity
        Menu existingMenu = mr.findById(menuItemId).orElse(null);

        // Step 2: Update the properties with values from MenuCsvDto
        if (existingMenu != null) {
            // Assuming MenuCsvDto has appropriate getter methods
            existingMenu.setPrice(menuItemDto.getPrice());
            // Update other properties as needed

            // Step 3: Save the updated Menu entity
            mr.save(existingMenu);
        } else {
            // Handle the case where the menu with the given MenuId is not found
            throw new EntityNotFoundException("Menu not found for provided MenuId");
        }
    }
}
