package com.university.ui.data.entity;


import com.university.ui.data.Role;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {


    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate dateOfBirth;
    private String nationality;
    private String seriesNo;
    private String idnp;
    private String address;
    private String phone;
    private String email;

    private Set<Role> roles;

    public Person(String firstName, String lastName, String patronymic, LocalDate dateOfBirth, String nationality, String seriesNo, String idnp, String address, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.seriesNo = seriesNo;
        this.idnp = idnp;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }


    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person that)) {
            return false; // null or not an AbstractEntity class
        }
        if (getId() != null) {
            return getId().equals(that.getId());
        }
        return super.equals(that);
    }
}
