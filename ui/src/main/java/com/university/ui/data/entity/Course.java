package com.university.ui.data.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Scope(value = "prototype")
public class Course {

    private int id;
    private String name;
    private int numCredit;
    private int numStudent;
    private int numHours;


}