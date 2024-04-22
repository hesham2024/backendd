package com.project.DinnerMe.controller;

import com.project.DinnerMe.CsvUtils;
import com.project.DinnerMe.entity.Restaurant;
import com.project.DinnerMe.model.MenuCsvDto;
import com.project.DinnerMe.model.RestaurantsCsvDto;
import com.project.DinnerMe.model.StaffCsvDto;
import com.project.DinnerMe.model.TablesCsvDto;
import com.project.DinnerMe.services.StaffService;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final String secretApiKey = "your-secret-api-key"; // Replace with your actual API key

    @Autowired
    private StaffService staffService;

    @PostMapping("/createAdminUser")
    public ResponseEntity<String> createAdminUser(@RequestParam("apiKey") String apiKey,
                                                  @RequestBody StaffCsvDto adminUserDto ,
                                                  @RequestParam("tablesCsvFile") MultipartFile tablesCsvFile,
                                                  @RequestParam("tablesCsvFile") MultipartFile restCsvFile,
                                                  @RequestParam("menusCsvFile") MultipartFile menusCsvFile) throws IOException {
        if (!secretApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API key");
        }
        // Create the admin user and save to the database
        RestaurantsCsvDto restCsvData = CsvUtils.readrestCsv(restCsvFile, RestaurantsCsvDto.class);
        Restaurant createdRestaurant = staffService.createRest(restCsvData);
        staffService.createAdminUser(adminUserDto,createdRestaurant);

        java.util.List<TablesCsvDto> tablesCsvData = CsvUtils.readCsv(tablesCsvFile,TablesCsvDto.class);
        staffService.createTables(tablesCsvData,createdRestaurant);

        // Process menus CSV file and create entities
        java.util.List<MenuCsvDto> menusCsvData = CsvUtils.readCsv(menusCsvFile, MenuCsvDto.class);
        staffService.createMenus(menusCsvData,createdRestaurant);

        return ResponseEntity.ok("Admin user created successfully.");
    }

}
