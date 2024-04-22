package com.project.DinnerMe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(StaffId.class)
public class Staff {
    @Id
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String fullName;
    @NotNull
    @Size(min = 11 ,max=11)
    private String phoneNumber;
    @NotNull
    private String role;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Length(min = 10)
    private String password;
    private boolean enable = false;

    public boolean getEnable() {
        return enable;
    }
}
