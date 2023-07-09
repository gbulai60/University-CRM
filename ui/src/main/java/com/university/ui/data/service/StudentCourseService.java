package com.university.ui.data.service;

import com.university.ui.data.entity.StudentCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentCourseService {
    Page<StudentCourse> list(Pageable pageable, String courseName, String studentIdnp);
    byte update(StudentCourse studentCourse);
    StudentCourse getStudentCourse(int courseId,String studentIdnp);
}
