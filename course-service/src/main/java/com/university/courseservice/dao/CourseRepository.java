package com.university.courseservice.dao;

import com.university.courseservice.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<Course,Integer>, JpaSpecificationExecutor<Course> {
    Page<Course> findAll(Pageable pageable);
    Page<Course> findAll(Specification<Course> filter,Pageable pageable);

}
