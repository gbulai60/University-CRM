package com.university.studentcourseservice.service;

import com.university.studentcourseservice.entity.CursStudentKey;
import com.university.studentcourseservice.entity.StudentCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface StudentCourseService {
     void save(StudentCourse studentCourse);
     void delete(int courseId,String studentIdnp);
     void deleteByCourseId(int courseId);
     void deleteByStudentIdnp(String studentIdnp);
     Page<StudentCourse> list(Pageable pageable);
     Page<StudentCourse> list(Specification<StudentCourse > specification, Pageable pageable);
     StudentCourse getStudentCourse(int courseId,String studentIdnp);

}
