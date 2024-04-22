package com.project.DinnerMe.services;

import com.project.DinnerMe.entity.*;
import com.project.DinnerMe.model.MenuCsvDto;
import com.project.DinnerMe.model.RestaurantsCsvDto;
import com.project.DinnerMe.model.StaffCsvDto;
import com.project.DinnerMe.model.TablesCsvDto;
import com.project.DinnerMe.repository.MenuRepository;
import com.project.DinnerMe.repository.RestaurantRepository;
import com.project.DinnerMe.repository.StaffRepository;
import com.project.DinnerMe.repository.TablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StaffService implements UserDetailsService {

    private final StaffRepository staffRepository;
    private final RestaurantRepository restaurantRepository;
    private final TablesRepository tablesRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StaffService(
            StaffRepository staffRepository,
            RestaurantRepository restaurantRepository,
            TablesRepository tablesRepository,
            MenuRepository menuRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.staffRepository = staffRepository;
        this.restaurantRepository = restaurantRepository;
        this.tablesRepository = tablesRepository;
        this.menuRepository = menuRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void createAdminUser(StaffCsvDto adminUserDto, Restaurant createdRestaurant) {
        Staff adminUser = mapDtoToEntity(adminUserDto,createdRestaurant);
        staffRepository.save(adminUser);
    }

    private Staff mapDtoToEntity(StaffCsvDto dto, Restaurant createdRestaurant) {
    Staff adminUser=new Staff();
    adminUser.setFullName(dto.getFullName());
        adminUser.setPhoneNumber(dto.getPhoneNumber());
        adminUser.setEmail(dto.getEmail());
        adminUser.setPassword(dto.getPassword());
        adminUser.setRole("ADMINR");
        adminUser.setRestaurant(createdRestaurant);

        return adminUser;

    }
    public Restaurant createRest(RestaurantsCsvDto restaurantsCsvDto) {
        // Convert StaffCsvDto to Staff entity
        Restaurant rest = mapTorestEntity(restaurantsCsvDto);
        return restaurantRepository.save(rest);
    }
    private Restaurant mapTorestEntity(RestaurantsCsvDto restaurantsCsvDto) {
        Restaurant rest = new Restaurant();
        rest.setName(restaurantsCsvDto.getName());
        rest.setPhoneNumber(restaurantsCsvDto.getPhoneNumber());
        rest.setAddress(restaurantsCsvDto.getAddress());
        rest.setOpen(restaurantsCsvDto.getOpen());
        rest.setClose(restaurantsCsvDto.getClose());
        rest.setTables(restaurantsCsvDto.getTables());
        return rest;
    }

    public void createTables(List<TablesCsvDto> tablesCsvData, Restaurant restaurant) {
        for (TablesCsvDto tablesCsvDto : tablesCsvData) {
            Tables table = mapToTablesEntity(tablesCsvDto, restaurant);
            tablesRepository.save(table);
        }
    }

    public void createMenus(List<MenuCsvDto> menusCsvData, Restaurant restaurant) {
        for (MenuCsvDto menuCsvDto : menusCsvData) {
            Menu menu = mapToMenuEntity(menuCsvDto, restaurant);
            menuRepository.save(menu);
        }
    }

    // Add the mapping methods for TablesCsvDto and MenuCsvDto to entity conversion

    private Tables mapToTablesEntity(TablesCsvDto tablesCsvDto, Restaurant restaurant) {
        Tables table = new Tables();
        // Set table properties using tablesCsvDto
        table.setId(tablesCsvDto.getTableId());
        table.setChairsNum(tablesCsvDto.getChairsNum());
        // Set the restaurant for which this table belongs
        table.setRestaurant(restaurant);
        return table;
    }

    private Menu mapToMenuEntity(MenuCsvDto menuCsvDto, Restaurant restaurant) {
        Menu menu = new Menu();
        // Set menu properties using menuCsvDto
        menu.setCategory(menuCsvDto.getCategory());
        menu.setFoodName(menuCsvDto.getFoodName());
        menu.setPrice(menuCsvDto.getPrice());
        // Set the restaurant for which this menu belongs
        menu.setRestaurant(restaurant);
        return menu;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public void addStaff(StaffCsvDto staffDto, Restaurant r) {
        Staff staff = new Staff();

        staff.setRestaurant(r);
        // Set other fields
        staff.setFullName(staffDto.getFullName());
        staff.setPhoneNumber(staffDto.getPhoneNumber());
        staff.setEmail(staffDto.getEmail());
        staff.setPassword(staffDto.getPassword());
        staff.setRole("STAFF");

        staffRepository.save(staff);
    }


    public void deleteStaff(StaffId staffId) {
        staffRepository.deleteById(staffId);
    }

    public Restaurant getRestaurantByEmail(String loggedInEmail) {
       return staffRepository.findByEmail(loggedInEmail).getRestaurant();
    }
}