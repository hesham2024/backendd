package com.project.DinnerMe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DinnerMe.entity.Client;
import com.project.DinnerMe.entity.favrest;
import com.project.DinnerMe.event.RegistrationCompleteEvent;
import com.project.DinnerMe.model.UserData;
import com.project.DinnerMe.services.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class hello {

    @Autowired
    ClientService cs;
    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserData> userHello() {
        // Retrieve the authenticated user's email
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        // Use the email to fetch user details
        Client c = cs.findUserByEmail(email);
        if (c != null) {
            // Create UserData object
            UserData userData = new UserData();
            userData.setFullName(c.getFullName());
            userData.setPhoneNumber(c.getPhoneNumber());
            userData.setId(c.getId());
            userData.setEmail(c.getEmail());
            userData.setAddress(c.getAddress());
            userData.setPhotoFilePath(c.getPhotoFilePath());
            userData.setBirthdate(c.getBirthdate());

            return ResponseEntity.ok(userData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/changeData")
    @PreAuthorize("hasRole('USER')")
    public String userHello(@RequestBody UserData ud, @RequestParam("mail") String mail, final HttpServletRequest req) {
        if(!mail.equalsIgnoreCase(ud.getEmail())){
            Client c=cs.findUserByEmail(ud.getEmail());
            if(c !=null){
                return "this email exist" ;
            }}

        String s = cs.updateUser(ud,mail);
        System.out.println(s);
        if (Objects.equals(s, "Data Changed")) {
            if (mail.equalsIgnoreCase(ud.getEmail())){
                return "success";
            }
            else{
                publisher.publishEvent(new RegistrationCompleteEvent(
                        cs.findUserByEmail(ud.getEmail()),
                        applicationUrl(req)
                ));
                return "Data Changed";
            }
        } else {
            return "error occurred";
        }
    }
    @PostMapping("/getfavrest")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userHello(@RequestParam("id") String id) {
        List<favrest> idfr = cs.getfavrests(id);
        // Check if the list is empty
        if (idfr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There are no favorite restaurants detected");
        } else {
            List<String> restaurantNames = new ArrayList<>();
            for (favrest favRest : idfr) {
                restaurantNames.add(favRest.getRestaurant().getName());
            }
            // Convert list to JSON array
            ObjectMapper objectMapper = new ObjectMapper();
            String json;
            try {
                json = objectMapper.writeValueAsString(restaurantNames);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error converting data to JSON.");
            }
            // Set content type as application/json
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(json, headers, HttpStatus.OK);
        }
    }
    @GetMapping("/admin/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminHello() {
        return "Hello, admin!";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort();
    }
}
