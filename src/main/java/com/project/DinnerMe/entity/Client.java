package com.project.DinnerMe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    @Size(min = 11, max = 11)
    private String phoneNumber;

    @NotNull
    private final String role = "USER";

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 10)
    private String password;

    private boolean enable = false;
    private LocalDate expirationDate;
    private String address;
    private LocalDate birthdate;

    private String photoFilePath;

    public boolean getEnable() {
        return enable;
    }
}
