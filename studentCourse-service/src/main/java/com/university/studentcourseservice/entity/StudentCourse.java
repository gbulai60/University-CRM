package com.university.studentcourseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(CursStudentKey.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    @Id
    private String studentIdnp;
    @Id
    private int courseId;
    private double average;


}
