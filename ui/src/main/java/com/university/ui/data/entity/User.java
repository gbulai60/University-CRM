package com.university.ui.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.university.ui.data.Role;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "application_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {

    private String username;
    private String name;
    private String idnp;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;





}
