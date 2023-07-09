package com.university.ui.data.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    private String studentIdnp;
    private int courseId;
    private double average;

    public StudentCourse(String studentIdnp, int courseId) {
        this.studentIdnp = studentIdnp;
        this.courseId = courseId;
    }
}
