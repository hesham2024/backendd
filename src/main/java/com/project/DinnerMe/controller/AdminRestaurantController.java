package com.project.DinnerMe.controller;

import com.project.DinnerMe.config.CustomUserDetails;
import com.project.DinnerMe.entity.MenuId;
import com.project.DinnerMe.entity.Restaurant;
import com.project.DinnerMe.entity.StaffId;
import com.project.DinnerMe.model.MenuCsvDto;
import com.project.DinnerMe.model.StaffCsvDto;
import com.project.DinnerMe.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin_rest")
public class AdminRestaurantController {
    @Autowired
    private StaffService staffService;

    @Autowired
    private MenuService menuService;

    // Add a new staff member
    @PostMapping("/addStaff")
    public ResponseEntity<String> addStaff(@RequestBody StaffCsvDto staffDto) {
       Restaurant r= staffService.getRestaurantByEmail(getLoggedInEmail());
        staffService.addStaff(staffDto,r);
        return ResponseEntity.ok("Staff member added successfully.");
    }

    // Delete a staff member
    @DeleteMapping("/deleteStaff/{staffId}")
    public ResponseEntity<String> deleteStaff(@PathVariable StaffId staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok("Staff member deleted successfully.");
    }

    // Add a new menu item
    @PostMapping("/addMenuItem")
    public ResponseEntity<String> addMenuItem(@RequestBody MenuCsvDto menuItemDto) {
        Restaurant r= staffService.getRestaurantByEmail(getLoggedInEmail());
        menuService.addMenuItem(menuItemDto,r);
        return ResponseEntity.ok("Menu item added successfully.");
    }

    // Delete a menu item
    @DeleteMapping("/deleteMenuItem/{menuItemId}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable MenuId menuItemId) {
        menuService.deleteMenuItem(menuItemId);
        return ResponseEntity.ok("Menu item deleted successfully.");
    }

    // Update a menu item
    @PutMapping("/updateMenuItem/{menuItemId}")
    public ResponseEntity<String> updateMenuItem(@PathVariable MenuId menuItemId, @RequestBody MenuCsvDto menuItemDto) {
        menuService.updateMenuItem(menuItemId, menuItemDto);
        return ResponseEntity.ok("Menu item updated successfully.");
    }
    String getLoggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null; // or handle accordingly
    }
}
