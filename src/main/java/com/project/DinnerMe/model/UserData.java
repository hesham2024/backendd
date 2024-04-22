package com.project.DinnerMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String fullName;
    private String phoneNumber;
    private Long id;
    private String email;
    private String photoFilePath;
    private String address;
    private LocalDate birthdate;
}
