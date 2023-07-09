package com.university.studentcourseservice.dao;

import com.university.studentcourseservice.entity.CursStudentKey;
import com.university.studentcourseservice.entity.StudentCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
public interface StudentCourseRepository extends JpaRepository<StudentCourse, CursStudentKey>, JpaSpecificationExecutor<StudentCourse> {

     Page<StudentCourse> findAll(Pageable pageable);
     Page<StudentCourse> findAll(Specification<StudentCourse > specification, Pageable pageable);
     void removeByCourseId(int courseId);
     void removeAllByStudentIdnp(String studentIdnp);
     StudentCourse getStudentCourseByCourseIdAndStudentIdnp(int courseId,String studentIdnp);

}
